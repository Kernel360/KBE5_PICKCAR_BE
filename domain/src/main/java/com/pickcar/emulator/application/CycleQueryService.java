package com.pickcar.emulator.application;

import com.pickcar.drivehistory.presentation.dto.payload.DriveHistoryPayload;
import com.pickcar.emulator.domain.Cycle;
import com.pickcar.emulator.infrastructure.CycleQueryRepository;
import com.pickcar.emulator.infrastructure.dto.CycleInfoProjection;
import com.pickcar.emulator.presentation.dto.context.PathContext;
import com.pickcar.emulator.infrastructure.dto.CycleProjection;
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

    public List<CycleProjection> getCyclesBetweenOnOffTime(DriveHistoryPayload payload) {
        return cycleQueryRepository.findAllByVehicleIdAndOccurredAtBetween(payload.getVehicleId(),
                payload.getEngineOnTime(), payload.getEngineOffTime());
    }

    public List<PathContext> extractPathContexts(List<Long> cycleIds) {
        List<CycleInfoProjection> projections = getCycleInfoProjectionsByIds(cycleIds);

        return projections.stream()
                .flatMap(projection -> projection.toPathContexts().stream())
                .toList();
    }

    private List<CycleInfoProjection> getCycleInfoProjectionsByIds(List<Long> ids) {
        return cycleQueryRepository.findCycleInfoProjections(ids);
    }
}
