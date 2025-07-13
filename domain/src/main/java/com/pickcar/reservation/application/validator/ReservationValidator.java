package com.pickcar.reservation.application.validator;

import com.pickcar.reservation.domain.ReservationStatus;
import com.pickcar.reservation.exception.ReservationErrorCode;
import com.pickcar.reservation.exception.ReservationException;
import com.pickcar.reservation.infrastructure.ReservationRepository;
import com.pickcar.reservation.presentation.dto.request.ReservationRequest;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationValidator {

    @Value(value = "${custom.reservation.maximum-due-date}")
    private Integer maximumDueDate;

    private final ReservationRepository reservationRepository;

    public void validateReservationRequest(ReservationRequest request) {
        isVehicleAlreadyReserved(request.vehicleId());
        isEmployeeAlreadyHasReservation(request.employeeId());
        validateDueDate(request.dueDate());
    }

    private void isVehicleAlreadyReserved(Long vehicleId) {
        boolean exists = reservationRepository
                .existsByVehicleIdAndStatusIn(vehicleId,
                        List.of(ReservationStatus.RESERVED, ReservationStatus.DELAYED));

        if (exists) {
            throw new ReservationException(ReservationErrorCode.VEHICLE_ALREADY_RESERVED);
        }
    }

    private void isEmployeeAlreadyHasReservation(Long employeeId) {
        boolean exists = reservationRepository
                .existsByUserIdAndStatusIn(employeeId, List.of(ReservationStatus.RESERVED, ReservationStatus.DELAYED));

        if (exists) {
            throw new ReservationException(ReservationErrorCode.EMPLOYEE_ALREADY_RESERVED);
        }
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
