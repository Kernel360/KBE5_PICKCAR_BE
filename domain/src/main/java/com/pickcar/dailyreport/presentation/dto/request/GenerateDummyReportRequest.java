package com.pickcar.dailyreport.presentation.dto.request;

import com.pickcar.dailyreport.domain.VehicleReservationStat;
import java.time.LocalDate;

public record GenerateDummyReportRequest(
        LocalDate reportDate,
        VehicleReservationStat vehicleReservationStat
) {
}
