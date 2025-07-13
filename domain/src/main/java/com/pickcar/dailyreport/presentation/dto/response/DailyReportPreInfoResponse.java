package com.pickcar.dailyreport.presentation.dto.response;

import com.pickcar.dailyreport.domain.DynamicInfo;
import com.pickcar.dailyreport.domain.VehicleReservationStat;

public record DailyReportPreInfoResponse(
        VehicleReservationStat currentStat,
        VehicleReservationStat yesterdayStat,
        DynamicInfo yesterdayDynamicInfo
) {
}