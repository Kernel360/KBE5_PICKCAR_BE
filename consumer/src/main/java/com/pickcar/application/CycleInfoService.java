package com.pickcar.application;

import com.pickcar.emulator.domain.Cycle;
import com.pickcar.infrastructure.CycleInfoRepository;
import com.pickcar.mq.dto.CycleInfoPayload;
import com.pickcar.mq.dto.CyclePayload;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
