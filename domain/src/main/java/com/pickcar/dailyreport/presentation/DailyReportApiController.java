package com.pickcar.dailyreport.presentation;

import com.pickcar.dailyreport.application.DailyReportService;
import com.pickcar.dailyreport.presentation.dto.response.DailyReportPreInfoResponse;
import com.pickcar.dailyreport.presentation.dto.response.VehicleReservationStatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class DailyReportApiController {

    private final DailyReportService dailyReportService;

    @GetMapping("/stat")
    @ResponseStatus(HttpStatus.OK)
    public VehicleReservationStatResponse vehicleReservationStat() {
        return dailyReportService.getVehicleReservationStatResponse();
    }

    @GetMapping("/pre-info")
    @ResponseStatus(HttpStatus.OK)
    public DailyReportPreInfoResponse preInfo() {
        return dailyReportService.getPreInfo();
    }
}
