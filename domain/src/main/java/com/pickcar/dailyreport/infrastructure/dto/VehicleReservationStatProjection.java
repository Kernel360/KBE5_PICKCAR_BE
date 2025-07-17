package com.pickcar.dailyreport.infrastructure.dto;

public record VehicleReservationStatProjection(
        Integer totalVehicleCount,
        Integer reservedVehiclesCount,
        Integer damagedVehiclesCount,
        Integer delayedCount,
        Integer expectedReturnInNext3Days
) {
}
