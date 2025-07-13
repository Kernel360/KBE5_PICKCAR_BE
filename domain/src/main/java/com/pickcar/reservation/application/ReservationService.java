package com.pickcar.reservation.application;

import com.pickcar.auth.application.AuthService;
import com.pickcar.auth.presentation.dto.response.UnAllocatedEmployeeResponse;
import com.pickcar.drivehistory.presentation.dto.request.DriveHistoryPayload;
import com.pickcar.reservation.domain.Reservation;
import com.pickcar.reservation.domain.ReservationStatus;
import com.pickcar.reservation.exception.ReservationErrorCode;
import com.pickcar.reservation.exception.ReservationException;
import com.pickcar.reservation.infrastructure.ReservationRepository;
import com.pickcar.reservation.presentation.dto.request.ReservationRequest;
import com.pickcar.reservation.presentation.dto.response.ReservationPreInfoResponse;
import com.pickcar.reservation.presentation.dto.response.SearchAbleVehiclesResponse;
import com.pickcar.security.jwt.JwtProvider;
import com.pickcar.vehicle.application.VehicleService;
import com.pickcar.vehicle.domain.Vehicle;
import com.pickcar.vehicle.domain.VehicleStatus;
import com.pickcar.vehicle.presentation.dto.response.UnAllocatedVehicleResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
        List<UnAllocatedVehicleResponse> vehicleInfos = vehicleService.getAllUnAllocatedVehicleInfos(
                allocatedVehicleIds);

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

    public Long getActiveReservationId(DriveHistoryPayload payload) {
        try {
            return getActiveReservationByVehicleIdAndUserId(payload.getVehicleId(), payload.getUserId());
        } catch (ReservationException e) {
            return getLatestValidReservation(payload.getVehicleId(), payload.getUserId());
        }
    }

    private Long getActiveReservationByVehicleIdAndUserId(Long vehicleId, Long userId) {
        List<ReservationStatus> validateStatuses = List.of(ReservationStatus.RESERVED, ReservationStatus.DELAYED);

        return reservationRepository.findIdByVehicleIdAndUserIdAndStatusIn(vehicleId, userId, validateStatuses)
                .orElseThrow(() -> new ReservationException(
                        ReservationErrorCode.NOT_FOUND_ACTIVE_RESERVATION_BY_VEHICLE_ID));
    }

    private Long getLatestValidReservation(Long vehicleId, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime coolDownMinutesAgo = now.minusMinutes(coolDownMinutes);

        return reservationRepository.findIdByVehicleIdAndUserIdAndStatusAndUpdatedAtBetween(
                        vehicleId, userId, ReservationStatus.RETURNED, coolDownMinutesAgo, now)
                .orElseThrow(() -> new ReservationException(ReservationErrorCode.NOT_FOUND_LATEST_UPDATED_RESERVATION));
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
