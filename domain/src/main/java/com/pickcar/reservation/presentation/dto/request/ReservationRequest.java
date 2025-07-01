package com.pickcar.reservation.presentation.dto.request;

public record ReservationRequest(
        Long employeeId,
        Long vehicleId
) {
}
