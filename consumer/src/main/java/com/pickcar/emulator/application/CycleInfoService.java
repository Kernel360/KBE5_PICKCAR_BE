package com.pickcar.emulator.application;

import com.pickcar.emulator.domain.Cycle;
import com.pickcar.emulator.dto.CyclePayload;
import com.pickcar.emulator.infrastructure.CycleInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CycleInfoService {

    private final CycleInfoRepository cycleInfoRepository;

    public void cycle(CyclePayload request) {
        Cycle cycleInfo = new Cycle(request.getVehicleId(), request.getOccurredAt(), request.getCycleCnt(), request.getCycleInfos());

        cycleInfoRepository.save(cycleInfo);
    }
}
