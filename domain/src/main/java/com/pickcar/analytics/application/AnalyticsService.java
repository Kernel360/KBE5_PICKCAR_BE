package com.pickcar.analytics.application;

import com.pickcar.analytics.presentation.dto.response.StaticAnalyticsResponse;
import com.pickcar.reservation.application.ReservationService;
import com.pickcar.vehicle.application.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final VehicleService vehicleService;
    private final ReservationService reservationService;

    public StaticAnalyticsResponse getStaticAnalytics() {

        Long totalVehicleCount = vehicleService.getAllCount();
        Long reservedVehiclesCount = reservationService.getReservedVehiclesCount();

    /* TODO:
            Integer totalVehicleCount,
            Integer reservedVehicleCount,
            Integer notOperableVehicleCount,
            Integer expectedReturnCount,
            Integer delayedCount
     */

        return new StaticAnalyticsResponse(totalVehicleCount, reservedVehiclesCount, null, null, null);
    }


}
