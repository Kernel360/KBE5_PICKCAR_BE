package com.pickcar.dailyreport.presentation;

import com.pickcar.dailyreport.application.DailyReportService;
import com.pickcar.dailyreport.domain.DailyReport;
import com.pickcar.dailyreport.presentation.dto.request.GenerateDummyReportRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class DailyReportApiController {

    private final DailyReportService dailyReportService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void getAnalytics() {
//        return dailyReportService.getStaticAnalytics(0L);
    }

    @PostMapping("/dummy")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void generateDummyReport(@RequestBody GenerateDummyReportRequest request) {
        dailyReportService.saveDummy(request);
    }
}
