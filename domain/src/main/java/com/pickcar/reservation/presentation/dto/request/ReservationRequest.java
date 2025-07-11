package com.pickcar.reservation.presentation.dto.request;

import java.time.LocalDate;

public record ReservationRequest(
        Long employeeId,
        Long vehicleId,
        LocalDate dueDate
) {
}
