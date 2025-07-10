package com.pickcar.analytics.application;

import com.pickcar.analytics.domain.StaticInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsAsyncService {

    private final AnalyticsService analyticsService;

    @Scheduled(cron = "1 0 0 * * *")
    public void collectInfos() {
        StaticInfo staticInfo = analyticsService.collectStaticInfos();
        analyticsService.collectNonStaticInfos();           // TODO: 기능 구현 필요
        analyticsService.save(staticInfo);
    }

}
