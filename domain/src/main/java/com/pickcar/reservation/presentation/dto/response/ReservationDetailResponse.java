package com.pickcar.reservation.presentation.dto.response;

import com.pickcar.reservation.infrastructure.dto.ReservationDetailProjection;
import com.pickcar.vehicle.domain.VehicleInfo;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReservationDetailResponse(
        Long reservationId,
        String employeeName,
        String phoneNumber,
        VehicleInfo vehicleInfo,
        LocalDate dueDate,
        LocalDateTime rentedAt
) {
}
