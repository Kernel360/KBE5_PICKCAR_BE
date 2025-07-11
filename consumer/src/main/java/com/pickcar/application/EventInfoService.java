package com.pickcar.application;

import com.pickcar.config.RestTemplateConfig;
import com.pickcar.dto.EventPayload;
import com.pickcar.emulator.domain.EventInfo;
import com.pickcar.infrastructure.EventInfoRepository;
import com.pickcar.presentation.dto.response.ErrorResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
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
        Optional<Long> offEventInfoId = saveEventInfo(request);
        offEventInfoId.ifPresent(this::writeDriveHistoryRequestAfterOff);
    }

    public void returned(EventPayload request) {
        saveEventInfo(request);
    }

    private Optional<Long> saveEventInfo(EventPayload request) {
        log.info("EventPayload: {}", request);
        Optional<EventInfo> getEventInfo = eventInfoRepository.findTopByVehicleIdOrderByIdDesc(
                request.getVehicleId());

        if (getEventInfo.isPresent() &&
                getEventInfo.get().getEventStatus().equals(request.getEventStatus())) {
            log.warn("동일한 상태의 Event가 이미 존재합니다. 운행일지 생성 생략.");
            return Optional.empty();
        }

        EventInfo eventInfo = toEventInfo(request);
        EventInfo save = eventInfoRepository.save(eventInfo);
        log.info("EventInfo: {}", save);
        return Optional.of(save.getId());
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

    public void writeDriveHistoryRequestAfterOff(Long offEventInfoId) {
        String url = deployDomain + "/api/v1/history/%d".formatted(offEventInfoId);
        log.info("Request URL : {}, OffEventInfoId : {}", url, offEventInfoId);
        try {
            restTemplate.postForEntity(url, null, Void.class);
            log.info("시동 OFF에 따른 운행일지 작성이 성공적으로 완료되었습니다. event ID : {}", offEventInfoId);
        } catch (Exception e) {
            log.error("운행일지 작성에 실패하였습니다. reason : {}", offEventInfoId);
        }
    }

}
