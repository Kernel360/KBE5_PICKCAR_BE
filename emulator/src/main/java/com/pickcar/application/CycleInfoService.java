package com.pickcar.application;

import com.pickcar.domain.Cycle;
import com.pickcar.infrastructure.CycleInfoRepository;
import com.pickcar.presentation.dto.request.CycleInfoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CycleInfoService {

    private final CycleInfoRepository cycleInfoRepository;

    public void cycle(CycleInfoRequest request) {
        Cycle cycleInfo = Cycle.builder()
                .carId(request.getCarId())
                .occurredAt(request.getOccurredAt())
                .cycleCnt(request.getCycleCnt())
                .cycleInfos(request.getCycleInfos())
                .build();
        cycleInfoRepository.save(cycleInfo);
    }
}
