package com.pickcar.dailyreport.application;

import com.pickcar.dailyreport.domain.DailyReport;
import com.pickcar.dailyreport.domain.DynamicInfo;
import com.pickcar.dailyreport.domain.VehicleReservationStat;
import java.time.LocalDate;
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
        DynamicInfo dynamicInfo = dailyReportService.collectDynamicInfos();
        dailyReportService.save(vehicleReservationStat, dynamicInfo);
    }
}
