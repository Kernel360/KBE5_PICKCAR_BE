package com.pickcar.dailyreport.application.mapper;

import com.pickcar.dailyreport.infrastructure.dto.VehicleReservationStatProjection;
import com.pickcar.dailyreport.presentation.dto.response.VehicleReservationStatResponse;
import org.springframework.stereotype.Component;

@Component
public class DailyReportResponseMapper {

    public VehicleReservationStatResponse toStatResponse(VehicleReservationStatProjection projection) {
        return new VehicleReservationStatResponse(projection);
    }
}
