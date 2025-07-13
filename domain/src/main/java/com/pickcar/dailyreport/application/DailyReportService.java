package com.pickcar.dailyreport.application;

import com.pickcar.dailyreport.application.mapper.DailyReportResponseMapper;
import com.pickcar.dailyreport.domain.DailyReport;
import com.pickcar.dailyreport.domain.DestinationCountStat;
import com.pickcar.dailyreport.domain.DriverAndDistanceContext;
import com.pickcar.dailyreport.domain.DynamicInfo;
import com.pickcar.dailyreport.domain.VehicleReservationStat;
import com.pickcar.dailyreport.infrastructure.DailyReportRepository;
import com.pickcar.dailyreport.infrastructure.dto.DestinationStatProjection;
import com.pickcar.dailyreport.infrastructure.dto.DriverAndDistanceProjection;
import com.pickcar.dailyreport.infrastructure.dto.PastVehicleReservationStatProjection;
import com.pickcar.dailyreport.infrastructure.dto.VehicleReservationStatProjection;
import com.pickcar.dailyreport.presentation.dto.response.VehicleReservationStatResponse;
import com.pickcar.drivehistory.application.service.DriveHistoryService;
import com.pickcar.drivehistory.domain.DriveHistory;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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
    public void save(VehicleReservationStat vehicleReservationStat, DynamicInfo dynamicInfo) {
        dailyReportRepository.save(new DailyReport(LocalDate.now(), vehicleReservationStat, dynamicInfo));
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

    public DynamicInfo collectDynamicInfos() {
        Double totalMovedDistance = calcTotalMovedDistance();
        List<DriverAndDistanceProjection> yesterdayTop3MovementContext = getYesterdayTop3MovementContext();
        List<DestinationStatProjection> yesterdayDestinationCounts = findYesterdayDestinationCounts();

        List<DriverAndDistanceContext> driverContexts = convertToDriverContexts(yesterdayTop3MovementContext);
        List<DestinationCountStat> destinationStats = convertToDestinationStats(yesterdayDestinationCounts);

        return new DynamicInfo(totalMovedDistance, driverContexts, destinationStats);
    }

    private Double calcTotalMovedDistance() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<DriveHistory> yesterdayHistories = driveHistoryService.getAllByDate(yesterday);

        return yesterdayHistories.stream()
                .mapToDouble(DriveHistory::getTotalDistance)
                .sum();
    }

    private List<DriverAndDistanceProjection> getYesterdayTop3MovementContext() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return dailyReportRepository.findTop3EmployeeNameAndTotalDistance(yesterday)
                .stream()
                .limit(3)
                .toList();
    }

    private List<DestinationStatProjection> findYesterdayDestinationCounts() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return dailyReportRepository.findYesterDayDestinationStats(yesterday);
    }

    private List<DriverAndDistanceContext> convertToDriverContexts(List<DriverAndDistanceProjection> projections) {
        return projections.stream()
                .map(projection -> new DriverAndDistanceContext(
                        projection.driverName(),
                        projection.totalDistance()
                ))
                .toList();
    }

    private List<DestinationCountStat> convertToDestinationStats(List<DestinationStatProjection> projections) {
        AtomicInteger rank = new AtomicInteger(1);
        return projections.stream()
                .map(projection -> new DestinationCountStat(
                        rank.getAndIncrement(), // 순위 자동 부여
                        projection.destination(),
                        projection.visitCount()
                ))
                .toList();
    }
}
