package com.pickcar.vehicle.presentation.dto.response;

import com.pickcar.vehicle.domain.Vehicle;
import com.pickcar.vehicle.domain.VehicleStatus;

public record UnAllocatedVehicleResponse(
        Long vehicleId,
        String licensePlate,
        String model,
        VehicleStatus status
) {

    public static UnAllocatedVehicleResponse from(Vehicle vehicle) {
        return new UnAllocatedVehicleResponse(
                vehicle.getId(),
                vehicle.getInfo().getLicensePlate(),
                vehicle.getInfo().getModel(),
                vehicle.getStatus()
        );
    }
}
