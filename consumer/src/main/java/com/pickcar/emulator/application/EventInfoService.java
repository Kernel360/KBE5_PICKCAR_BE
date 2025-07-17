package com.pickcar.emulator.application;

import com.pickcar.common.application.DomainCommandPublisher;
import com.pickcar.emulator.dto.EventPayload;
import com.pickcar.emulator.domain.EventInfo;
import com.pickcar.emulator.infrastructure.EventInfoRepository;
import com.pickcar.presentation.dto.response.ErrorResponse;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventInfoService {

    @Value("${http.endpoint.domain}")
    private String deployDomain;

    private final EventInfoRepository eventInfoRepository;
    private final RestTemplate restTemplate;
    private final DomainCommandPublisher domainCommandPublisher;

    public void on(EventPayload request) {
        saveEventInfo(request);
    }

    public void off(EventPayload request, Long userId) {
        Optional<EventInfo> offEventInfo = saveEventInfo(request);
        offEventInfo.ifPresent(eventInfo -> {
            domainCommandPublisher.publishDriveHistory(eventInfo, userId);
        });
    }

    public void returned(EventPayload request, Long userId) {
        EventInfo eventInfo = saveEventInfo(request).get();
        domainCommandPublisher.publishReservationReturn(userId, eventInfo.getVehicleId());
    }

    private void submitReturn(Long userId, Long vehicleId) {
        String requestUrl = deployDomain + "/api/v1/reservation/return/" + vehicleId;

        //FIXME: RestTemplate 말고 다른 방식으로 진행되도록
        HttpHeaders headers = new HttpHeaders();
        headers.put("userId", List.of(userId.toString()));
        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

        try {
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            restTemplate.patchForObject(requestUrl, requestEntity, Void.class);;
        } catch(HttpClientErrorException e) {
            Optional<ErrorResponse> errorResponse = ErrorResponse.parseHttpStatusCodeException(e);
            if (errorResponse.isPresent()) {
                log.warn("반납 처리에 실패하였습니다 : {}", errorResponse.get().errorReason().reason());
                return;
            }
            log.warn("반납 처리와 요청 정보 파싱에 실패하였습니다. responseBody : {}", e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("예기치 않은 서버 오류가 발생하였습니다.");
        }
    }

    private Optional<EventInfo> saveEventInfo(EventPayload request) {
        Optional<EventInfo> getEventInfo = eventInfoRepository.findTopByVehicleIdOrderByIdDesc(
                request.getVehicleId());

        if (getEventInfo.isPresent() &&
                getEventInfo.get().getEventStatus().equals(request.getEventStatus())) {
            log.warn("동일한 상태의 Event가 이미 존재합니다. 운행일지 생성 생략.");
            return Optional.empty();
        }

        EventInfo eventInfo = toEventInfo(request);
        EventInfo save = eventInfoRepository.save(eventInfo);
        log.info("EventInfo: {}", save.toString());
        return Optional.of(save);
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
