package com.pickcar.dailyreport.presentation.dto.response;

import com.pickcar.dailyreport.domain.DynamicInfo;
import com.pickcar.dailyreport.domain.VehicleReservationStat;
import com.pickcar.dailyreport.infrastructure.dto.MovedDistanceHistoryProjection;
import java.time.LocalDate;
import java.util.List;

public record DailyReportPreInfoResponse(
        VehicleReservationStat currentStat,
        VehicleReservationStat yesterdayStat,
        DynamicInfo yesterdayDynamicInfo,
        List<MovedDistanceHistoryProjection> movedDistances,
        LocalDate yesterday
) {
}