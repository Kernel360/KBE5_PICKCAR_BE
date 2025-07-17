package com.pickcar.reservation.infrastructure.dto;

import com.pickcar.vehicle.domain.VehicleInfo;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReservationDetailProjection(
        Long reservationId,
        String employeeName,
        String employeePhoneNumber,
        VehicleInfo vehicleInfo,
        LocalDate dueDate,
        LocalDateTime rentedAt
) {
}
