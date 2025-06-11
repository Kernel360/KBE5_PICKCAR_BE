package com.pickcar.vehicle.presentation.dto.request;

import com.pickcar.vehicle.domain.VehicleStatus;

public record ChangeVehicleStatusRequest(
        Long vehicleId,
        VehicleStatus vehicleStatus
) {
}
