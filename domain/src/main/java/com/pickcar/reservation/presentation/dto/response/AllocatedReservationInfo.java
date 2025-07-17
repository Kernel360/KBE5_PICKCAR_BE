package com.pickcar.reservation.presentation.dto.response;

import com.pickcar.reservation.domain.ReservationStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AllocatedReservationInfo(
        Long vehicleId,
        LocalDateTime rentedAt,
        LocalDate dueDate,
        ReservationStatus status
) {
}
