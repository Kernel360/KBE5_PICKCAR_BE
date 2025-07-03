package com.pickcar.application;

import com.pickcar.config.RestTemplateConfig;
import com.pickcar.dto.EventPayload;
import com.pickcar.emulator.domain.EventInfo;
import com.pickcar.infrastructure.EventInfoRepository;
import java.util.Optional;
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

    @Value("${http.endpoint.domain}")
    private String deployDomain;

    private final EventInfoRepository eventInfoRepository;
    private final RestTemplate restTemplate;

    public void on(EventPayload request) {
        saveEventInfo(request);
    }

    public void off(EventPayload request) {
        EventInfo eventInfo = saveEventInfo(request);
        writeDriveHistoryRequestAfterOff(eventInfo);
    }

    public void returned(EventPayload request) {
        saveEventInfo(request);
    }

    private EventInfo saveEventInfo(EventPayload request) {
        log.info("Saving event info: {}", request);
        try {
            Optional<EventInfo> getEventInfo = eventInfoRepository.findTopByVehicleIdOrderByIdDesc(request.getVehicleId());

            getEventInfo.ifPresent(info -> {
                if (info.getEventStatus().equals(request.getEventStatus())) {
                    log.error("EventInfo 저장 실패: {}", request);
                    throw new RuntimeException("이미 동일한 상태입니다.");
                }
            });

            EventInfo eventInfo = toEventInfo(request);
            return eventInfoRepository.save(eventInfo);
        } catch (Exception e) {
            log.error("EventInfo 저장 실패: {}", e.getMessage(), e);
            // TODO: 커스텀 예외 고려
            throw e;
        }
    }

    private static EventInfo toEventInfo(EventPayload request) {
        return EventInfo.builder()
                .vehicleId(request.getVehicleId())
                .eventStatus(request.getEventStatus())
                .engineOnTime(request.getEngineOnTime())
                .engineOffTime(request.getEngineOffTime())
                .gpsStatus(request.getGpsStatus())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();
    }

    public void writeDriveHistoryRequestAfterOff(EventInfo offEventInfo) {
        String url = deployDomain + "/api/v1/history/%d".formatted(offEventInfo.getId());
        log.info("driving history request to: {}", url);
        try {
            restTemplate.postForEntity(url, null, Void.class);
        } catch (Exception e) {
            log.error("EventInfo 전송 실패: {}", e.getMessage(), e);
        }
    }

}
