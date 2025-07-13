package com.pickcar.vehicle.presentation.dto.response;

import com.pickcar.vehicle.domain.Vehicle;
import com.pickcar.vehicle.domain.VehicleStatus;

public record SearchAbleVehiclesResponse(
        Long vehicleId,
        String licensePlate,
        String model,
        VehicleStatus status
) {

    public static SearchAbleVehiclesResponse from(Vehicle vehicle) {
        return new SearchAbleVehiclesResponse(
                vehicle.getId(),
                vehicle.getInfo().getLicensePlate(),
                vehicle.getInfo().getModel(),
                vehicle.getStatus()
        );
    }
}
