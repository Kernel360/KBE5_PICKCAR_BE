package com.pickcar.reservation.infrastructure.dto;

import com.pickcar.auth.domain.UserRole;

public record EmployeeReservationProjection(
        Long reservationId,
        Long employeeId,
        String name,
        String email,
        UserRole role,
        Boolean hasReservation,
        String licensePlate
) {
}
