package com.pickcar.vehicle.infrastructure.dto;

import com.pickcar.vehicle.domain.VehicleInfo;

public record AvailableVehicleProjection(
        Long vehicleId,
        VehicleInfo vehicleInfo
) {
}
