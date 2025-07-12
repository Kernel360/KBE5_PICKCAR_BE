package com.pickcar.reservation.application;

import com.pickcar.auth.application.AuthService;
import com.pickcar.auth.domain.User;
import com.pickcar.auth.domain.UserInfo;
import com.pickcar.auth.presentation.dto.response.UnAllocatedEmployeeResponse;
import com.pickcar.reservation.domain.Reservation;
import com.pickcar.reservation.domain.ReservationStatus;
import com.pickcar.reservation.exception.ReservationErrorCode;
import com.pickcar.reservation.exception.ReservationException;
import com.pickcar.reservation.infrastructure.ReservationRepository;
import com.pickcar.reservation.presentation.dto.context.ReservationContext;
import com.pickcar.reservation.presentation.dto.request.ReservationRequest;
import com.pickcar.reservation.presentation.dto.response.ReservationPreInfoResponse;
import com.pickcar.reservation.presentation.dto.response.SearchAbleVehiclesResponse;
import com.pickcar.security.jwt.JwtProvider;
import com.pickcar.vehicle.application.VehicleService;
import com.pickcar.vehicle.domain.Vehicle;
import com.pickcar.vehicle.domain.VehicleInfo;
import com.pickcar.vehicle.domain.VehicleStatus;
import com.pickcar.vehicle.presentation.dto.response.UnAllocatedVehicleResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    @Value(value = "${custom.reservation.cool-down-minutes}")
    private Integer coolDownMinutes;

    @Value(value = "${custom.reservation.maximum-due-date}")
    private Integer maximumDueDate;

    private final AuthService authService;
    private final VehicleService vehicleService;
    private final JwtProvider jwtProvider;
    private final ReservationRepository reservationRepository;

    @Transactional
    public void reservation(ReservationRequest request) {

        //FIXME: 권한 분리 및 유효성검사 통합 메서드 필요
        //이미 할당된 차량이 있는 회원에 대해
        if (hasAlreadyReservation(request.employeeId())) {
            throw new ReservationException(ReservationErrorCode.EMPLOYEE_ALREADY_RESERVED);
        }

        //이미 할당처리가 된 차량에 대해
        if (isAlreadyReserved(request.vehicleId())) {
            throw new ReservationException(ReservationErrorCode.VEHICLE_ALREADY_RESERVED);
        }

        validateDueDate(request.dueDate());

        Reservation reservation = Reservation.builder()
                .userId(request.employeeId())
                .vehicleId(request.vehicleId())
                .rentedAt(LocalDateTime.now())
                .dueDate(request.dueDate())
                .returnedAt(null)
                .status(ReservationStatus.RESERVED)
                .build();

        reservationRepository.save(reservation);
    }

    @Transactional
    public void submitReturn(HttpServletRequest servletRequest, Long vehicleId) {
        Long userId = jwtProvider.extractUserId(servletRequest);
        Reservation reservation = getReturnTarget(userId, vehicleId);
        reservation.submitReturn();
    }

    public ReservationPreInfoResponse getReservationPreInfos() {
        //FIXME: ID 리스트와 VEHICLE 리스트를 한 번에 가져오도록.
        // 여기에 더해 사실장 이 response를 한 번에 가져올 수 있도록 변경 필요
        List<Long> allocatedUserIds = reservationRepository.findUserIdsByStatusIn(
                List.of(ReservationStatus.RESERVED, ReservationStatus.DELAYED));

        List<Long> allocatedVehicleIds = reservationRepository.findVehicleIdsByStatusIn(
                List.of(ReservationStatus.RESERVED, ReservationStatus.DELAYED));

        List<UnAllocatedEmployeeResponse> employeeInfos = authService.getUnAllocatedEmployeeInfos(allocatedUserIds);
        List<UnAllocatedVehicleResponse> vehicleInfos = vehicleService.getAllUnAllocatedVehicleInfos(allocatedVehicleIds);

        return new ReservationPreInfoResponse(employeeInfos, vehicleInfos);
    }

    private Reservation getReturnTarget(Long userId, Long vehicleId) {
        Optional<Reservation> maybeReservation = reservationRepository.findByUserIdAndVehicleIdAndStatusIn(userId,
                vehicleId, List.of(ReservationStatus.RESERVED, ReservationStatus.DELAYED));

        if (maybeReservation.isEmpty()) {
            throw new ReservationException(ReservationErrorCode.UNAUTHORIZED_FOR_RETURN);
        }

        return maybeReservation.get();
    }

    public Reservation getById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] Reservation Not Found By Id " + id));
    }

    public Reservation getActiveReservationForDriveHistory(Long vehicleId, Long userId) {
        try {
            return getActiveReservation(vehicleId);
        } catch (ReservationException e) {
            return getLatestValidReservation(vehicleId);
            //TODO: 여기서도 찾지 못했다면 어떻게 대처할 것인지
        }
    }

    private Reservation getActiveReservation(Long vehicleId) {
        Optional<Reservation> maybeReservation = reservationRepository.findByVehicleIdAndStatus(vehicleId,
                ReservationStatus.RESERVED);

        if (maybeReservation.isPresent()) {
            //FIXME: Optional.get()을 두 번 사용중
            return maybeReservation.get();
        }

        throw new ReservationException(ReservationErrorCode.NOT_FOUND_ACTIVE_RESERVATION_BY_VEHICLE_ID);
    }

    private Reservation getLatestValidReservation(Long vehicleId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime coolDownMinutesAgo = now.minusMinutes(coolDownMinutes);

        Optional<Reservation> maybeReservation = reservationRepository.findByVehicleIdAndUpdatedAtBetween(
                vehicleId, coolDownMinutesAgo, now);

        if (maybeReservation.isPresent()) {
            //FIXME: Optional.get()을 두 번 사용중
            return maybeReservation.get();
        }

        throw new ReservationException(ReservationErrorCode.NOT_FOUND_LATEST_UPDATED_RESERVATION);
    }

    public ReservationContext getReservationContextById(Long reservationId) {
        Reservation reservation = getById(reservationId);
        User user = authService.getById(reservation.getUserId());
        Vehicle vehicle = vehicleService.getById(reservation.getVehicleId());

        return new ReservationContext(reservation, user.getInfo(), vehicle.getInfo());
    }

    private List<ReservationContext> getContextsByIds(List<Reservation> reservations, Map<Long, UserInfo> userInfoMap,
                                                      Map<Long, VehicleInfo> vehicleInfoMap) {
        return reservations.stream()
                .map(reservation -> {
                    UserInfo userInfo = userInfoMap.get(reservation.getUserId());
                    VehicleInfo vehicleInfo = vehicleInfoMap.get(reservation.getVehicleId());
                    return new ReservationContext(reservation, userInfo, vehicleInfo);
                }).toList();
    }

    public Map<Long, ReservationContext> getContextMapByIds(List<Long> reservationIds) {
        List<Reservation> reservations = getAllByIds(reservationIds);
        Map<Long, UserInfo> userInfoMap = extractUserInfoMap(reservations);
        Map<Long, VehicleInfo> vehicleInfoMap = extractVehicleInfoMap(reservations);
        List<ReservationContext> contexts = getContextsByIds(reservations, userInfoMap, vehicleInfoMap);

        return contexts.stream()
                .collect(Collectors.toMap(
                        ReservationContext::getReservationId,
                        Function.identity()
                ));
    }

    private Map<Long, UserInfo> extractUserInfoMap(List<Reservation> reservations) {
        List<Long> userIds = reservations.stream()
                .map(Reservation::getUserId)
                .distinct()
                .toList();

        List<User> users = authService.getAllByIds(userIds);

        return users.stream()
                .collect(Collectors.toMap(User::getId, User::getInfo));
    }

    private Map<Long, VehicleInfo> extractVehicleInfoMap(List<Reservation> reservations) {
        List<Long> vehicleIds = reservations.stream()
                .map(Reservation::getVehicleId)
                .distinct()
                .toList();

        List<Vehicle> vehicles = vehicleService.getAllByIds(vehicleIds);

        return vehicles.stream()
                .collect(Collectors.toMap(Vehicle::getId, Vehicle::getInfo));
    }

    private List<Reservation> getAllByIds(List<Long> reservationIds) {
        return reservationRepository.findAllById(reservationIds);
    }

    public List<SearchAbleVehiclesResponse> getAbleVehicles() {
        //운행 가능한 상태의 차면서 예약 상태가 아닌 것
        List<Vehicle> availableVehicles = reservationRepository.findAvailableVehicles(VehicleStatus.OPERABLE,
                ReservationStatus.RESERVED);

        return availableVehicles.stream()
                .map(SearchAbleVehiclesResponse::from)
                .toList();
    }

    public List<SearchAbleVehiclesResponse> getAssignedVehicles() {
        //운행 가능한 상태의 차면서 예약 상태인 것
        List<Vehicle> availableVehicles = reservationRepository.findAssignedVehicles(VehicleStatus.OPERABLE,
                ReservationStatus.RESERVED);

        return availableVehicles.stream()
                .map(SearchAbleVehiclesResponse::from)
                .toList();
    }

    private boolean isAlreadyReserved(Long vehicleId) {
        return reservationRepository.findByVehicleIdAndStatus(vehicleId, ReservationStatus.RESERVED).isPresent();
    }

    private boolean hasAlreadyReservation(Long employeeId) {
        return reservationRepository.findByUserIdAndStatus(employeeId, ReservationStatus.RESERVED).isPresent();
    }

    private void validateDueDate(LocalDate dueDate) {
        if (dueDate.isBefore(LocalDate.now())) {
            throw new ReservationException(ReservationErrorCode.DUE_DATE_CANNOT_BE_FUTURE);
        }

        if (dueDate.isAfter(LocalDate.now().plusDays(maximumDueDate))) {
            throw new ReservationException(ReservationErrorCode.DUE_DATE_OVER_MAXIMUM);
        }
    }
}
