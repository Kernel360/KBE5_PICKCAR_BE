package com.pickcar.dailyreport.infrastructure.dto;

public record StaticInfoProjection(
        Integer totalVehicleCount,
        Integer reservedVehiclesCount,
        Integer delayedCount,
        Integer expectedReturnInNext3Days
) {
}
