package com.pickcar.dailyreport.infrastructure.dto;

import com.pickcar.dailyreport.domain.VehicleReservationStat;

public record PastVehicleReservationStatProjection(
        VehicleReservationStat stat
) {
}
