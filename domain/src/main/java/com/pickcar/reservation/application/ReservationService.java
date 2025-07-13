package com.pickcar.reservation.application;

import com.pickcar.auth.application.AuthService;
import com.pickcar.auth.presentation.dto.response.UnAllocatedEmployeeResponse;
import com.pickcar.dto.command.DriveHistoryWriteCommand;
import com.pickcar.dto.command.ReservationReturnCommand;
import com.pickcar.reservation.application.validator.ReservationValidator;
import com.pickcar.reservation.domain.Reservation;
import com.pickcar.reservation.domain.ReservationStatus;
import com.pickcar.reservation.exception.ReservationErrorCode;
import com.pickcar.reservation.exception.ReservationException;
import com.pickcar.reservation.infrastructure.ReservationRepository;
import com.pickcar.reservation.presentation.dto.request.ReservationRequest;
import com.pickcar.reservation.presentation.dto.response.ReservationPreInfoResponse;
import com.pickcar.reservation.presentation.dto.response.SearchAbleVehiclesResponse;
import com.pickcar.vehicle.application.VehicleService;
import com.pickcar.vehicle.domain.Vehicle;
import com.pickcar.vehicle.domain.VehicleStatus;
import com.pickcar.vehicle.presentation.dto.response.UnAllocatedVehicleResponse;
import java.time.LocalDateTime;
import java.util.List;
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

    private final AuthService authService;
    private final VehicleService vehicleService;
    private final ReservationValidator validator;
    private final ReservationRepository reservationRepository;

    @Transactional
    public void reserving(ReservationRequest request) {
        validator.validateReservationRequest(request);

        Reservation reservation = Reservation.builder()
                .userId(request.employeeId())
                .vehicleId(request.vehicleId())
                .rentedAt(LocalDateTime.now())
                .dueDate(request.dueDate())
                .returnedAt(null)
                .status(ReservationStatus.RESERVED)
                .build();

        vehicleService.processRented(request.vehicleId());
        reservationRepository.save(reservation);
    }

    @Transactional
    public void processReturn(ReservationReturnCommand command) {
        Reservation reservation = getReservationForReturn(command.employeeId(), command.vehicleId());
        vehicleService.processReturned(command.vehicleId());
        reservation.markAsReturned();
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

    private Reservation getReservationForReturn(Long userId, Long vehicleId) {
        return reservationRepository.findByUserIdAndVehicleIdAndStatusIn(userId, vehicleId,
                        List.of(ReservationStatus.RESERVED, ReservationStatus.DELAYED))
                .orElseThrow(() -> new ReservationException(ReservationErrorCode.UNAUTHORIZED_FOR_RETURN));
    }

    public Long getActiveReservationId(DriveHistoryWriteCommand command) {
        try {
            return getActiveReservationByVehicleIdAndUserId(command.vehicleId(), command.userId());
        } catch (ReservationException e) {
            return getLatestValidReservation(command.vehicleId(), command.userId());
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

    public List<SearchAbleVehiclesResponse> getAssignedVehicles() {
        //운행 가능한 상태의 차면서 예약 상태인 것
        List<Vehicle> availableVehicles = reservationRepository.findAssignedVehicles(VehicleStatus.OPERABLE,
                List.of(ReservationStatus.RESERVED, ReservationStatus.DELAYED));

        return availableVehicles.stream()
                .map(SearchAbleVehiclesResponse::from)
                .toList();
    }
}
