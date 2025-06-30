package com.pickcar.application;

import com.pickcar.config.RestTemplateConfig;
import com.pickcar.dto.EventPayload;
import com.pickcar.emulator.domain.EventInfo;
import com.pickcar.infrastructure.EventInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventInfoService {

    private final RestTemplateConfig restTemplateConfig;

    @Value("${custom.deploy.domain}")
    private String deployDomain;

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
        EventInfo offEventInfo = EventInfo.builder()
                .vehicleId(request.getVehicleId())
                .status(request.getStatus())
                .engineOnTime(request.getEngineOnTime())
                .engineOffTime(request.getEngineOffTime())
                .gpsStatus(request.getGpsStatus())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        eventInfoRepository.save(offEventInfo);
        writeDriveHistoryRequestAfterOff(offEventInfo);
    }

    public void writeDriveHistoryRequestAfterOff(EventInfo offEventInfo) {
        RestTemplate restTemplate = restTemplateConfig.restTemplate();
        restTemplate.postForEntity(deployDomain + "/api/v1/history/%d".formatted(offEventInfo.getId()),
                null, Void.class);
    }
}
