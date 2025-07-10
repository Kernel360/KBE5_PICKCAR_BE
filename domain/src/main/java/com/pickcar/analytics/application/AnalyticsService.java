package com.pickcar.analytics.application;

import com.pickcar.analytics.presentation.dto.response.StaticAnalyticsResponse;
import com.pickcar.reservation.application.ReservationService;
import com.pickcar.reservation.domain.ReservationStatus;
import com.pickcar.vehicle.application.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final VehicleService vehicleService;
    private final ReservationService reservationService;

    public StaticAnalyticsResponse getStaticAnalytics() {

        //FIXME: 각 서비스를 호출하며 JPA 조회를 하지 않고 JPA 없이 Analytics 레포지토를 만들어서 context를 한 번에 조회하도록
        Long totalVehicleCount = vehicleService.getAllCount();
        Long reservedVehiclesCount = reservationService.getReservedVehiclesCount();
        Long expectedReturnCount = reservationService.getExpectedReturnCount();
        Long delayedCount = reservationService.getDelayedCount();

    /* TODO:
            Integer delayedCount
     */

        return new StaticAnalyticsResponse(
                totalVehicleCount,
                reservedVehiclesCount,
                totalVehicleCount - reservedVehiclesCount,
                expectedReturnCount,
                delayedCount);
    }


}
