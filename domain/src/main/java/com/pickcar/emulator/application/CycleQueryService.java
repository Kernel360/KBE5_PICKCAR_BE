package com.pickcar.emulator.application;

import com.pickcar.drivehistory.presentation.dto.request.DriveHistoryPayload;
import com.pickcar.emulator.infrastructure.CycleQueryRepository;
import com.pickcar.emulator.infrastructure.dto.CycleInfoProjection;
import com.pickcar.emulator.infrastructure.dto.CycleProjection.TotalCycleData;
import com.pickcar.emulator.infrastructure.dto.PathContext;
import com.pickcar.emulator.infrastructure.dto.CycleProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CycleQueryService {

    private final CycleQueryRepository cycleQueryRepository;

    public TotalCycleData getCyclesBetweenOnOffTime(DriveHistoryPayload payload) {
        List<CycleProjection> cycleProjections = cycleQueryRepository.findAllByVehicleIdAndOccurredAtBetween(
                payload.getVehicleId(),
                payload.getEngineOnTime(), payload.getEngineOffTime());

        if(cycleProjections.isEmpty()) {
            return TotalCycleData.empty();
        }

        return new TotalCycleData(cycleProjections);
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
