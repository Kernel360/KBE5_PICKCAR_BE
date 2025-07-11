package com.pickcar.dailyreport.application;

import com.pickcar.dailyreport.domain.DailyReport;
import com.pickcar.dailyreport.domain.StaticInfo;
import com.pickcar.dailyreport.infrastructure.DailyReportRepository;
import com.pickcar.dailyreport.presentation.dto.request.GenerateDummyReportRequest;
import com.pickcar.drivehistory.application.DriveHistoryService;
import com.pickcar.drivehistory.domain.DriveHistory;
import com.pickcar.reservation.application.ReservationService;
import com.pickcar.vehicle.application.VehicleService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
    private final DriveHistoryService driveHistoryService;
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
        dailyReportRepository.save(new DailyReport(request.reportDate(), request.staticInfo()));

        setUpDynamicInfos();
    }

    public void setUpDynamicInfos() {
        /*
            TODO:
                1. 최근 일주일 간 모든 운행의 총 운행 KM 수를 각각 점 그래프로 - totalMovedDistance
                2. 어제자 기준 가장 이용량이 많은 사원 TOP 3 - 총 이동 거리
                3. 전일 기준 지역별 차량 분포 (권역/구역별 차량 배치 현황)
         */

        //어제자 총 운행 km 수
        Double movedDistance = calcTotalMovedDistance();

        log.info("Total moved distance: {}", movedDistance);
    }

    private Double calcTotalMovedDistance() {
        Double movedDistance = 0.0D;
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<DriveHistory> yesterdayHistory = driveHistoryService.getAllByDate(yesterday);

        for(DriveHistory history : yesterdayHistory) {
            movedDistance += history.getTotalDistance();
        }

        return movedDistance;
    }

}
