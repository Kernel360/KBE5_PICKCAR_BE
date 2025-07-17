package com.pickcar.dailyreport.application.mapper;

import com.pickcar.dailyreport.domain.DynamicInfo;
import com.pickcar.dailyreport.domain.VehicleReservationStat;
import com.pickcar.dailyreport.infrastructure.dto.MovedDistanceHistoryProjection;
import com.pickcar.dailyreport.infrastructure.dto.VehicleReservationStatProjection;
import com.pickcar.dailyreport.presentation.dto.response.DailyReportPreInfoResponse;
import com.pickcar.dailyreport.presentation.dto.response.VehicleReservationStatResponse;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DailyReportResponseMapper {

    public VehicleReservationStatResponse toStatResponse(VehicleReservationStatProjection projection) {
        return VehicleReservationStatResponse.from(projection);
    }

    public DailyReportPreInfoResponse toPreInfoResponse(VehicleReservationStat currentStat,
                                                        VehicleReservationStat yesterdayStat,
                                                        DynamicInfo yesterdayDynamicInfo,
                                                        List<MovedDistanceHistoryProjection> distanceProjections) {
        return new DailyReportPreInfoResponse(currentStat, yesterdayStat, yesterdayDynamicInfo, distanceProjections,
                LocalDate.now().minusDays(1));
    }
}
