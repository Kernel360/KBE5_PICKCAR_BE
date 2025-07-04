package com.pickcar.emulator.presentation.context;

import com.pickcar.dto.CycleInfoPayload;
public record PathContext(
        Double longitude,
        Double latitude
) {

    public static PathContext of(CycleInfoPayload cycleInfo) {
        return new PathContext(cycleInfo.getLongitude(), cycleInfo.getLatitude());
    }
}
