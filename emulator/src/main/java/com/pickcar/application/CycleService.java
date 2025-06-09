package com.pickcar.application;

import com.pickcar.domain.Cycle;
import com.pickcar.domain.CycleInfo;
import com.pickcar.domain.EventInfo;
import com.pickcar.infrastructure.CycleRepository;
import com.pickcar.presentation.dto.request.CycleStoreRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CycleService {

    private final CycleRepository cycleRepository;

    public void store(CycleStoreRequest request) {
        Cycle cycle = Cycle.builder()
                .vehicleId(request.getVehicleId())
                .occurredAt(request.getOccurredAt())
                .cycleCnt(request.getCycleCnt())
                .distance(calcDistance(request.getCycleInfos()))
                .cycleInfos(request.getCycleInfos())
                .build();

        cycleRepository.save(cycle);
    }

    public List<Cycle> getCyclesByOffEventInfo(EventInfo offEventInfo) {
        return cycleRepository.findAllByVehicleIdAndOccurredAtBetween(offEventInfo.getVehicleId(),
                offEventInfo.getEngineOnTime(), offEventInfo.getEngineOffTime());
    }

    private Double calcDistance(List<CycleInfo> cycleInfos) {
        double totalDistance = 0.0D;

        for(CycleInfo cycleInfo : cycleInfos) {
            totalDistance += (cycleInfo.getLongitude() + cycleInfo.getLatitude());
        }

        return totalDistance;
    }

    public Cycle getById(Long id) {
        return cycleRepository.findById(id).orElse(null);       //FIXME: NULL 예외처리
    }
}
