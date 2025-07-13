package com.pickcar.reservation.application;

import com.pickcar.auth.domain.UserRole;
import com.pickcar.dto.command.DriveHistoryWriteCommand;
import com.pickcar.dto.command.ReservationReturnCommand;
import com.pickcar.reservation.application.mapper.ReservationResponseMapper;
import com.pickcar.reservation.application.validator.ReservationValidator;
import com.pickcar.reservation.domain.Reservation;
import com.pickcar.reservation.domain.ReservationStatus;
import com.pickcar.reservation.exception.ReservationErrorCode;
import com.pickcar.reservation.exception.ReservationException;
import com.pickcar.reservation.infrastructure.ReservationRepository;
import com.pickcar.reservation.infrastructure.dto.EmployeeReservationProjection;
import com.pickcar.reservation.infrastructure.dto.ReservationDetailProjection;
import com.pickcar.reservation.presentation.dto.request.ReservationRequest;
import com.pickcar.reservation.presentation.dto.response.ReservationDetailResponse;
import com.pickcar.reservation.presentation.dto.response.ReservationPreInfoResponse;
import com.pickcar.vehicle.presentation.dto.response.SearchAbleVehiclesResponse;
import com.pickcar.vehicle.application.VehicleService;
import com.pickcar.vehicle.domain.Vehicle;
import com.pickcar.vehicle.domain.VehicleStatus;
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

    private final VehicleService vehicleService;
    private final ReservationValidator validator;
    private final ReservationResponseMapper responseMapper;
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
        List<EmployeeReservationProjection> projections = reservationRepository.findEmployeesWithReservationPreInfo(
                UserRole.EMPLOYEE, List.of(ReservationStatus.RESERVED, ReservationStatus.DELAYED));

        return responseMapper.toPreInfoResponse(projections);
    }

    public Long getActiveReservationId(DriveHistoryWriteCommand command) {
        try {
            return getActiveReservationByVehicleIdAndUserId(command.vehicleId(), command.userId());
        } catch (ReservationException e) {
            return getLatestValidReservation(command.vehicleId(), command.userId());
        }
    }

    public ReservationDetailResponse getDetailResponse(Long reservationId) {
        ReservationDetailProjection projection = reservationRepository.findReservationDetailById(reservationId)
                .orElseThrow(() -> new ReservationException(ReservationErrorCode.NOT_FOUND_BY_ID));

        return responseMapper.toDetailResponse(projection);
    }

    private Reservation getReservationForReturn(Long userId, Long vehicleId) {
        return reservationRepository.findByUserIdAndVehicleIdAndStatusIn(userId, vehicleId,
                        List.of(ReservationStatus.RESERVED, ReservationStatus.DELAYED))
                .orElseThrow(() -> new ReservationException(ReservationErrorCode.UNAUTHORIZED_FOR_RETURN));
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
}
