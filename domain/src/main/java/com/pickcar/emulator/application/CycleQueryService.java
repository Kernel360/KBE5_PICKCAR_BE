package com.pickcar.emulator.application;

import com.pickcar.dto.command.DriveHistoryWriteCommand;
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

    public TotalCycleData getCyclesBetweenOnOffTime(DriveHistoryWriteCommand command) {
        List<CycleProjection> cycleProjections = cycleQueryRepository.findAllByVehicleIdAndOccurredAtBetween(
                command.vehicleId(), command.engineOnTime(), command.engineOffTime());

        if (cycleProjections.isEmpty()) {
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
