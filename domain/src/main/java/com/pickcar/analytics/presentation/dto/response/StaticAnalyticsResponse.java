package com.pickcar.analytics.presentation.dto.response;

public record StaticAnalyticsResponse(
        Integer totalVehicleCount,
        Integer reservedVehicleCount,
        Integer notOperableVehicleCount,
        Integer expectedReturnCount,
        Integer delayedCount
) {
}
