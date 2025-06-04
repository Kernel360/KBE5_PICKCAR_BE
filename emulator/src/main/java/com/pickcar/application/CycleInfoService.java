package com.pickcar.application;

import com.pickcar.domain.CycleInfo;
import com.pickcar.domain.EventInfo;
import com.pickcar.infrastructure.CycleInfoRepository;
import com.pickcar.presentation.dto.request.CycleInfoRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CycleInfoService {

    private final CycleInfoRepository cycleInfoRepository;

    public void cycle(CycleInfoRequest request) {
        CycleInfo cycleInfo = CycleInfo.builder()
                .vehicleId(request.getCarId())
                .occurredAt(request.getOccurredAt())
                .cycleCnt(request.getCycleCnt())
                .cycleInfos(request.getCycleInfos())
                .build();
        cycleInfoRepository.save(cycleInfo);
    }

    public List<CycleInfo> getCycleInfosByOffEventInfo(EventInfo offEventInfo) {
        return cycleInfoRepository.findByVehicleIdAndOccurredAtBetween(offEventInfo.getVehicleId(),
                offEventInfo.getEngineOnTime(), offEventInfo.getEngineOffTime());
    }
}
