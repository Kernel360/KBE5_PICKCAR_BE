package com.pickcar.vehicle.presentation.dto.response;

import com.pickcar.vehicle.domain.Vehicle;
import com.pickcar.vehicle.domain.VehicleStatus;
import com.pickcar.vehicle.infrastructure.dto.AssignedVehiclesProjection;

public record SearchAbleVehiclesResponse(
        Long vehicleId,
        String licensePlate,
        String model,
        VehicleStatus status
) {
    public static SearchAbleVehiclesResponse from(AssignedVehiclesProjection projection) {
        return new SearchAbleVehiclesResponse(
                projection.vehicleId(),
                projection.vehicleInfo().getLicensePlate(),
                projection.vehicleInfo().getModel(),
                projection.status()
        );
    }
}
