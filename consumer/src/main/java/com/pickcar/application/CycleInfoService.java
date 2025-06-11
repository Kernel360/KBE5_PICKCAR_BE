package com.pickcar.application;

import com.pickcar.emulator.domain.Cycle;
import com.pickcar.infrastructure.CycleInfoRepository;
import com.pickcar.mq.dto.CyclePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CycleInfoService {

    private final CycleInfoRepository cycleInfoRepository;

    public void cycle(CyclePayload request) {
        Cycle cycleInfo = Cycle.builder()
                .vehicleId(request.getVehicleId())
                .occurredAt(request.getOccurredAt())
                .cycleCnt(request.getCycleCnt())
                .distance(request.getDistance())
                .cycleInfos(request.getCycleInfos())
                .build();
        cycleInfoRepository.save(cycleInfo);
    }
}
