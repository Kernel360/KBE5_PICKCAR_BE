package com.pickcar.analytics.presentation.dto.response;

public record StaticAnalyticsResponse(
        Long totalVehicleCount,
        Long reservedVehicleCount,
        Long notOperableVehicleCount,
        Long expectedReturnCount,
        Long delayedCount
) {
}
