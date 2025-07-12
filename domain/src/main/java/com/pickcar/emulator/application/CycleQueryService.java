package com.pickcar.emulator.application;

import com.pickcar.drivehistory.domain.DriveHistory;
import com.pickcar.drivehistory.presentation.dto.payload.DriveHistoryPayload;
import com.pickcar.emulator.domain.Cycle;
import com.pickcar.emulator.domain.EventInfo;
import com.pickcar.emulator.infrastructure.CycleQueryRepository;
import com.pickcar.emulator.presentation.context.PathContext;
import com.pickcar.reservation.domain.Reservation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CycleQueryService {

    private final CycleQueryRepository cycleQueryRepository;

    public Cycle getById(Long cycleId) {
        return cycleQueryRepository.findById(cycleId).orElseThrow(
                () -> new IllegalArgumentException("cycle not found")
        );
    }

    public Double getTotalDistanceForHistory(DriveHistoryPayload payload) {
        return getCyclesBetweenOnOffTime(payload).stream()
                .mapToDouble(Cycle::getDistance)
                .sum();
    }

    private List<Cycle> getCyclesBetweenOnOffTime(DriveHistoryPayload payload) {
        return cycleQueryRepository.findAllByVehicleIdAndOccurredAtBetween(payload.getVehicleId(),
                payload.getEngineOnTime(), payload.getEngineOffTime());
    }

    public List<PathContext> getPathsByReservationAndHistory(Reservation reservation, DriveHistory driveHistory) {
        LocalDateTime startedAt = driveHistory.getDrivingStartedAt();
        LocalDateTime endedAt = driveHistory.getDrivingEndedAt();
        Long vehicleId = reservation.getVehicleId();

        List<Cycle> cycles = cycleQueryRepository.findAllByVehicleIdAndOccurredAtBetween(vehicleId, startedAt, endedAt);
        List<PathContext> paths = new ArrayList<>();

        cycles.stream()
                .forEach((cycle) -> {
                    cycle.getCycleInfos().stream()
                                    .forEach((cycleInfo) -> {
                                        PathContext path = PathContext.of(cycleInfo);
                                        paths.add(path);
                                    });
                });

        return paths;
    }
}
