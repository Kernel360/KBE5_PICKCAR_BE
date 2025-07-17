package com.pickcar.emulator.infrastructure.dto;

import com.pickcar.dto.CycleInfoPayload;
import java.util.List;

public record CycleInfoProjection(
        List<CycleInfoPayload> cycleInfos
) {
    public List<PathContext> toPathContexts() {
        return cycleInfos.stream()
                .map(info -> new PathContext(info.getLongitude(), info.getLatitude()))
                .toList();
    }
}
