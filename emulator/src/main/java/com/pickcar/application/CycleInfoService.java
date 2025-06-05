package com.pickcar.application;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pickcar.domain.Cycle;
import com.pickcar.domain.CycleInfo;
import com.pickcar.domain.EventInfo;
import com.pickcar.infrastructure.CycleRepository;
import com.pickcar.presentation.dto.request.CycleInfoRequest;
import jakarta.persistence.Table;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Table(name = "cycles")
@RequiredArgsConstructor
public class CycleInfoService {

    private final CycleRepository cycleRepository;

    public void cycle(CycleInfoRequest request) throws IOException {

        // NOTE: distance를 가져오는게 아니라 request 상태에서 꺼내서 계산해야 한다.
        Double totalDistance = calcDistance(request.getCycleCnt(), request.getCycleInfos());

        ObjectMapper mapper = new ObjectMapper();
        String str = mapper.writeValueAsString(request.getCycleInfos());

        Cycle cycle = Cycle.builder()
                .vehicleId(request.getVehicleId())
                .occurredAt(request.getOccurredAt())
                .cycleCnt(request.getCycleCnt())
                .distance(totalDistance)
                .cycleInfos(str)
                .build();

        cycleRepository.save(cycle);
    }

    public List<Cycle> getCycleInfosByOffEventInfo(EventInfo offEventInfo) {
        return cycleRepository.findByVehicleIdAndOccurredAtBetween(offEventInfo.getVehicleId(),
                offEventInfo.getEngineOnTime(), offEventInfo.getEngineOffTime());
    }

    private Double calcDistance(int cycleCnt, Map<String, Object> cycleInfos) throws IOException {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());      //FIXME: mapper가 계속 생성될 필요는 X
        double totalDistance = 0.0D;

        for(int i=0; i<cycleCnt; i++) {
            CycleInfo currentInfo = mapper.convertValue(cycleInfos.get(String.valueOf(i)), CycleInfo.class);        //FIXME: deserializer 필요

            totalDistance += (currentInfo.getLongitude() + currentInfo.getLatitude());        //FIXME: (테스트용) => 계산식 사용
        }

        return totalDistance;
    }

    public Cycle getById(Long id) {
        return cycleRepository.findById(id).orElse(null);       //FIXME: NULL 예외처리
    }
}
