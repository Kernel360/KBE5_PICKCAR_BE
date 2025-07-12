package com.pickcar.drivehistory.application.query;

import com.pickcar.drivehistory.presentation.dto.payload.DriveHistoryPayload;
import com.pickcar.emulator.application.CycleQueryService;
import com.pickcar.emulator.infrastructure.dto.CycleProjection;
import com.pickcar.emulator.infrastructure.dto.CycleProjection.TotalCycleData;
import com.pickcar.emulator.presentation.dto.context.PathContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CycleInfoQuery {

    private final CycleQueryService cycleQueryService;

    public TotalCycleData findCycleInfo(DriveHistoryPayload payload) {
        List<CycleProjection> cycles = cycleQueryService.getCyclesBetweenOnOffTime(payload);
        return new TotalCycleData(cycles);
    }

    public List<PathContext> findPathsByCycleIds(List<Long> cycleIds) {
        return cycleQueryService.extractPathContexts(cycleIds);
    }
}
