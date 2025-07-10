package com.pickcar.analytics.presentation;

import com.pickcar.analytics.application.AnalyticsService;
import com.pickcar.analytics.presentation.dto.response.StaticAnalyticsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsApiController {

    private final AnalyticsService analyticsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public StaticAnalyticsResponse getAnalytics() {
        return analyticsService.getStaticAnalytics();
    }
}
