package com.pickcar.application;

import com.pickcar.domain.Cycle;
import com.pickcar.domain.CycleInfo;
import com.pickcar.domain.EventInfo;
import com.pickcar.infrastructure.CycleInfoConverter;
import com.pickcar.infrastructure.CycleRepository;
import com.pickcar.presentation.dto.request.CycleInfoRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CycleInfoService {

    private final CycleRepository cycleRepository;
    private final CycleInfoConverter cycleInfoConverter;

    public void cycle(CycleInfoRequest request) throws IOException {
        Cycle cycle = Cycle.builder()
                .vehicleId(request.getVehicleId())
                .occurredAt(request.getOccurredAt())
                .cycleCnt(request.getCycleCnt())
                .distance(calcDistance(request.getCycleCnt(), request.getCycleInfos()))
                .cycleInfos(cycleInfoConverter.convertMapToString(request.getCycleInfos()))
                .build();

        cycleRepository.save(cycle);
    }

    public List<Cycle> getCycleInfosByOffEventInfo(EventInfo offEventInfo) {
        return cycleRepository.findByVehicleIdAndOccurredAtBetween(offEventInfo.getVehicleId(),
                offEventInfo.getEngineOnTime(), offEventInfo.getEngineOffTime());
    }

    private Double calcDistance(int cycleCnt, Map<String, Object> cycleInfos) throws IOException {
        double totalDistance = 0.0D;

        for (int i = 0; i < cycleCnt; i++) {
            CycleInfo cycleInfo = cycleInfoConverter.convertRawMapToCycleInfo(cycleInfos.get(String.valueOf(i)));
            totalDistance += (cycleInfo.getLongitude() + cycleInfo.getLatitude());
        }

        return totalDistance;
    }

    public Cycle getById(Long id) {
        return cycleRepository.findById(id).orElse(null);       //FIXME: NULL 예외처리
    }
}
