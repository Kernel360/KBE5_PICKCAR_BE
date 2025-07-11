package com.pickcar.dailyreport.application;

import com.pickcar.dailyreport.domain.DailyReport;
import com.pickcar.dailyreport.domain.StaticInfo;
import com.pickcar.dailyreport.infrastructure.DailyReportRepository;
import com.pickcar.dailyreport.presentation.dto.request.GenerateDummyReportRequest;
import com.pickcar.reservation.application.ReservationService;
import com.pickcar.vehicle.application.VehicleService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyReportService {

    private final VehicleService vehicleService;
    private final ReservationService reservationService;
    private final DailyReportRepository dailyReportRepository;

    @Transactional
    public void save(StaticInfo staticInfo) {
        dailyReportRepository.save(new DailyReport(LocalDate.now(), staticInfo));
    }

    public StaticInfo collectStaticInfos() {
        //FIXME: 각 서비스를 호출하며 JPA 조회를 하지 않고 JPA 없이 Analytics 레포지토리를 만들어서 context를 한 번에 조회하도록
        Long totalVehicleCount = vehicleService.getAllCount();
        Long reservedVehiclesCount = reservationService.getReservedVehiclesCount();
        Long expectedReturnCount = reservationService.getExpectedReturnCount();
        Long delayedCount = reservationService.getDelayedCount();

        return new StaticInfo(
                totalVehicleCount,
                reservedVehiclesCount,
                (totalVehicleCount - reservedVehiclesCount),
                expectedReturnCount,
                delayedCount);
    }

    public void collectNonStaticInfos() {
        //TODO: 정적 데이터 수집
    }

    public void getStaticAnalytics(Long vehicleId) {
        return;
    }

    public void saveDummy(GenerateDummyReportRequest request) {

//        LocalDate reportDate,
//        Long totalVehicleCount,
//        Long reservedVehicleCount,
//        Long notOperableVehicleCount,
//        Long expectedReturnCount,
//        Long delayedCount

        log.info(request.staticInfo().getDelayedCount().toString());

        dailyReportRepository.save(new DailyReport(request.reportDate(), request.staticInfo()));
    }

}
