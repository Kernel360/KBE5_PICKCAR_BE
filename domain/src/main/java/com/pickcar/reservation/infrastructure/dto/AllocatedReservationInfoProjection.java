package com.pickcar.reservation.infrastructure.dto;

import com.pickcar.reservation.domain.ReservationStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AllocatedReservationInfoProjection(
        Long vehicleId,
        LocalDateTime rentedAt,
        LocalDate dueDate,
        ReservationStatus status
) {
}
