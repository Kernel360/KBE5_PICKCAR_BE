package com.pickcar.dailyreport.presentation.dto.response;

import com.pickcar.dailyreport.infrastructure.dto.VehicleReservationStatProjection;

public record VehicleReservationStatResponse(
        Integer totalVehicleCount,
        Integer reservedVehicleCount,
        Integer notOperableVehicleCount,
        Integer expectedReturnCount,
        Integer delayedCount
) {
    public static VehicleReservationStatResponse from(VehicleReservationStatProjection projection) {
        return new VehicleReservationStatResponse(
                projection.totalVehicleCount(),
                projection.reservedVehiclesCount(),
                projection.totalVehicleCount() - projection.reservedVehiclesCount(),
                projection.expectedReturnInNext3Days(),
                projection.delayedCount()
        );
    }
}
