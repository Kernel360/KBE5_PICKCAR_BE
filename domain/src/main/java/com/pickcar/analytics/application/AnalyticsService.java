package com.pickcar.analytics.application;

import com.pickcar.analytics.domain.Analytics;
import com.pickcar.analytics.domain.StaticInfo;
import com.pickcar.analytics.infrastructure.AnalyticsRepository;
import com.pickcar.analytics.presentation.dto.response.StaticAnalyticsResponse;
import com.pickcar.reservation.application.ReservationService;
import com.pickcar.reservation.domain.ReservationStatus;
import com.pickcar.vehicle.application.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final VehicleService vehicleService;
    private final ReservationService reservationService;
    private final AnalyticsRepository analyticsRepository;

    @Transactional
    public void save(StaticInfo staticInfo) {
        analyticsRepository.save(new Analytics(staticInfo));
    }

    private StaticInfo collectStaticInfos() {
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

    @Scheduled(cron = "1 0 0 * * *")
    public void collectInfos() {
        StaticInfo staticInfo = collectStaticInfos();
        collectNonStaticInfos();
        save(staticInfo);
    }

    private void collectNonStaticInfos() {
        //TODO: 정적 데이터 수집
    }

    public StaticAnalyticsResponse getStaticAnalytics(Long vehicleId) {
        return null;
    }

}
