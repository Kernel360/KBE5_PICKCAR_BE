package com.pickcar.vehicle.presentation.dto.response;

import com.pickcar.vehicle.domain.Vehicle;
import com.pickcar.vehicle.domain.VehicleInfo;
import com.pickcar.vehicle.domain.VehicleStatus;
import java.time.LocalDate;

public record VehicleListResponse(
        Long vehicleId,
        String licensePlate,
        String model,
        String color,
        VehicleStatus vehicleStatus,
        Boolean isRented,
        LocalDate createdAt
) {
    public static VehicleListResponse from(Vehicle vehicle) {
        VehicleInfo info = vehicle.getInfo();
        return new VehicleListResponse(vehicle.getId(), info.getLicensePlate(), info.getModel(),
                info.getColor(), vehicle.getStatus(), vehicle.getIsRented(), vehicle.getCreatedAt().toLocalDate());
    }
}
