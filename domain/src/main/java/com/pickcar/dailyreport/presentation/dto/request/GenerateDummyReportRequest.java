package com.pickcar.dailyreport.presentation.dto.request;

import com.pickcar.dailyreport.domain.StaticInfo;
import java.time.LocalDate;

public record GenerateDummyReportRequest(
        LocalDate reportDate,
        StaticInfo staticInfo
) {
}
