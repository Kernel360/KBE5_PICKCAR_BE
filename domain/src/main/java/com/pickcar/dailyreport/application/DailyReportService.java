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
import com.pickcar.dailyreport.infrastructure.dto.VehicleReservationStatProjection;
import com.pickcar.dailyreport.presentation.dto.response.DailyReportPreInfoResponse;
import com.pickcar.dailyreport.presentation.dto.response.VehicleReservationStatResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final DailyReportRepository dailyReportRepository;

    @Transactional
    public void save(VehicleReservationStat vehicleReservationStat, DynamicInfo dynamicInfo) {
        dailyReportRepository.save(new DailyReport(LocalDate.now(), vehicleReservationStat, dynamicInfo));
    }

    public DailyReportPreInfoResponse getPreInfo() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        DailyReport dailyReport = getByReportDate(yesterday);
        VehicleReservationStat currentStat = collectVehicleReservationStat();
        VehicleReservationStat yesterdayStat = dailyReport.getVehicleReservationStat();
        DynamicInfo yesterdayDynamicInfo = dailyReport.getDynamicInfo();

        return responseMapper.toPreInfoResponse(currentStat, yesterdayStat, yesterdayDynamicInfo);
    }

    private DailyReport getByReportDate(LocalDate yesterday) {
        return dailyReportRepository.findByReportDate(yesterday)
                .orElseThrow(() -> new IllegalArgumentException("해당 일자 통계 데이터를 찾을 수 없습니다."));
    }

    public VehicleReservationStatResponse getVehicleReservationStatResponse() {
        VehicleReservationStatProjection projection = getVehicleReservationStat();
        return responseMapper.toStatResponse(projection);
    }

    private VehicleReservationStatProjection getVehicleReservationStat() {
        LocalDate today = LocalDate.now();
        LocalDate after3Days = today.plusDays(3);
        return dailyReportRepository.findVehicleReservationStat(today, after3Days);
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
        LocalDateTime startOfDay = yesterday.atStartOfDay();
        LocalDateTime endOfDay = yesterday.atTime(23, 59, 59);

        return dailyReportRepository.sumTotalDistanceByDate(startOfDay, endOfDay);
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
        return dailyReportRepository.findYesterdayDestinationStats(yesterday);
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
