package com.pickcar.application;

import com.pickcar.domain.EventInfo;
import com.pickcar.infrastructure.EventInfoRepository;
import com.pickcar.mq.dto.EventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventInfoService {

    private final EventInfoRepository eventInfoRepository;

    public void on(EventPayload request) {
        EventInfo eventInfo = EventInfo.builder()
                .vehicleId(request.getVehicleId())
                .status(request.getStatus())
                .engineOnTime(request.getEngineOnTime())
                .engineOffTime(request.getEngineOffTime())
                .gpsStatus(request.getGpsStatus())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();
        eventInfoRepository.save(eventInfo);
    }

    public void off(EventPayload request) {
        EventInfo eventInfo = EventInfo.builder()
                .vehicleId(request.getVehicleId())
                .status(request.getStatus())
                .engineOnTime(request.getEngineOnTime())
                .engineOffTime(request.getEngineOffTime())
                .gpsStatus(request.getGpsStatus())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();
        eventInfoRepository.save(eventInfo);
    }
}
