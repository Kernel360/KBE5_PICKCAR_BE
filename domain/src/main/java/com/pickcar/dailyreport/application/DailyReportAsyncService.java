package com.pickcar.dailyreport.application;

import com.pickcar.dailyreport.domain.VehicleReservationStat;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DailyReportAsyncService {

    private final DailyReportService dailyReportService;

    @Scheduled(cron = "1 0 0 * * *")
    public void collectInfos() {
        VehicleReservationStat vehicleReservationStat = dailyReportService.collectVehicleReservationStat();
        dailyReportService.collectNonStaticInfos();           // TODO: 기능 구현 필요
        dailyReportService.save(vehicleReservationStat);
    }
}
