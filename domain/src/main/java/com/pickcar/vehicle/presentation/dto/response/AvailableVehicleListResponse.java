package com.pickcar.vehicle.presentation.dto.response;

import com.pickcar.vehicle.infrastructure.dto.AvailableVehicleProjection;

public record AvailableVehicleListResponse(
        Long vehicleId,
        String licensePlate,
        String model,
        String color,
        String carAge
) {
    public static AvailableVehicleListResponse from(AvailableVehicleProjection projection) {
        return new AvailableVehicleListResponse(
                projection.vehicleId(),
                projection.vehicleInfo().getLicensePlate(),
                projection.vehicleInfo().getModel(),
                projection.vehicleInfo().getColor(),
                projection.vehicleInfo().getCarAge()
        );
    }
}
