package com.pickcar.vehicle.presentation.dto.response;

import com.pickcar.vehicle.domain.VehicleStatus;
import java.time.LocalDate;

public record VehicleListResponse(
        Long vehicleId,
        String licensePlate,
        String model,
        String color,
        VehicleStatus vehicleStatus,
        String rentedCompany,
        LocalDate rentedAt
) {
}
