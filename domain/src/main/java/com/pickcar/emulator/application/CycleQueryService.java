package com.pickcar.emulator.application;

import com.pickcar.emulator.domain.Cycle;
import com.pickcar.emulator.domain.EventInfo;
import com.pickcar.emulator.infrastructure.CycleQueryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CycleQueryService {

    private final CycleQueryRepository cycleQueryRepository;

    public Cycle getById(Long cycleId) {
        return cycleQueryRepository.findById(cycleId).orElseThrow(
                () -> new IllegalArgumentException("cycle not found")
        );
    }

    public Double getTotalDistanceForHistory(EventInfo offEventInfo) {
        return getCyclesBetweenOnOffTime(offEventInfo).stream()
                .mapToDouble(Cycle::getDistance)
                .sum();
    }

    private List<Cycle> getCyclesBetweenOnOffTime(EventInfo offEventInfo) {
        return cycleQueryRepository.findAllByVehicleIdAndOccurredAtBetween(offEventInfo.getVehicleId(),
                offEventInfo.getEngineOnTime(), offEventInfo.getEngineOffTime());
    }
}
