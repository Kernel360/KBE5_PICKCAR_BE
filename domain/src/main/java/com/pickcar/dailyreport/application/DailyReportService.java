package com.pickcar.dailyreport.application;

import com.pickcar.dailyreport.application.mapper.DailyReportResponseMapper;
import com.pickcar.dailyreport.domain.DailyReport;
import com.pickcar.dailyreport.domain.VehicleReservationStat;
import com.pickcar.dailyreport.infrastructure.DailyReportRepository;
import com.pickcar.dailyreport.infrastructure.dto.DestinationStatProjection;
import com.pickcar.dailyreport.infrastructure.dto.PastVehicleReservationStatProjection;
import com.pickcar.dailyreport.infrastructure.dto.VehicleReservationStatProjection;
import com.pickcar.dailyreport.presentation.dto.request.GenerateDummyReportRequest;
import com.pickcar.dailyreport.presentation.dto.response.VehicleReservationStatResponse;
import com.pickcar.drivehistory.application.service.DriveHistoryService;
import com.pickcar.drivehistory.domain.DriveHistory;
import com.pickcar.drivehistory.presentation.dto.context.Top3DriverDistanceContext;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyReportService {

    private final DailyReportResponseMapper responseMapper;
    private final DriveHistoryService driveHistoryService;
    private final DailyReportRepository dailyReportRepository;

    @Transactional
    public void save(VehicleReservationStat vehicleReservationStat) {
        dailyReportRepository.save(new DailyReport(LocalDate.now(), vehicleReservationStat));
    }

    public void getPreInfo() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        VehicleReservationStatProjection projection = getVehicleReservationStat();
        PastVehicleReservationStatProjection yesterdayProjection = dailyReportRepository.findStatByDate(yesterday);
    }

    public VehicleReservationStatResponse getVehicleReservationStatResponse() {
        VehicleReservationStatProjection projection = getVehicleReservationStat();
        return responseMapper.toStatResponse(projection);
    }

    private VehicleReservationStatProjection getVehicleReservationStat() {
        LocalDate today = LocalDate.now();
        LocalDate after3Days = today.plusDays(3);
        return dailyReportRepository.findStaticInfo(today, after3Days);
    }

    public VehicleReservationStat collectVehicleReservationStat() {
        VehicleReservationStatProjection projection = getVehicleReservationStat();
        return new VehicleReservationStat(
                projection.totalVehicleCount(),
                projection.reservedVehiclesCount(),
                projection.totalVehicleCount() - projection.reservedVehiclesCount(),
                projection.expectedReturnInNext3Days(),
                projection.delayedCount()
        );
    }

    public void collectNonStaticInfos() {
        //TODO: 정적 데이터 수집
    }

    public void getStaticAnalytics(Long vehicleId) {
        return;
    }

    public void saveDummy(GenerateDummyReportRequest request) {
        dailyReportRepository.save(new DailyReport(request.reportDate(), request.vehicleReservationStat()));

        setUpDynamicInfos();
    }

    public void setUpDynamicInfos() {
        Double totalMovedDistance = calcTotalMovedDistance();
        List<Top3DriverDistanceContext> yesterdayTop3MovementContext = getYesterdayTop3MovementContext();
        List<DestinationStatProjection> yesterdayDestinationCounts = findYesterdayDestinationCounts();
    }

    private List<DestinationStatProjection> findYesterdayDestinationCounts() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return dailyReportRepository.findYesterDayDestinationStats(yesterday);
    }

    private Double calcTotalMovedDistance() {
        Double movedDistance = 0.0D;
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<DriveHistory> yesterdayHistory = driveHistoryService.getAllByDate(yesterday);

        for (DriveHistory history : yesterdayHistory) {
            movedDistance += history.getTotalDistance();
        }

        return movedDistance;
    }

    private List<Top3DriverDistanceContext> getYesterdayTop3MovementContext() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return driveHistoryService.getTop3MovementInfo(yesterday);
    }
}
