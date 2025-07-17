package com.pickcar.vehicle.infrastructure.dto;

import com.pickcar.vehicle.domain.VehicleInfo;
import com.pickcar.vehicle.domain.VehicleStatus;

public record AssignedVehiclesProjection(
        Long vehicleId,
        VehicleInfo vehicleInfo,
        VehicleStatus status
) {
}
