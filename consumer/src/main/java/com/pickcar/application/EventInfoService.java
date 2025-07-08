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
        EventInfo eventInfo = saveEventInfo(request);
        writeDriveHistoryRequestAfterOff(eventInfo);
    }

    public void returned(EventPayload request) {
        saveEventInfo(request);
    }

    private EventInfo saveEventInfo(EventPayload request) {
        log.info("Saving event info: {}", request);
        try {
            Optional<EventInfo> getEventInfo = eventInfoRepository.findTopByVehicleIdOrderByIdDesc(
                    request.getVehicleId());

            getEventInfo.ifPresent(info -> {
                if (info.getEventStatus().equals(request.getEventStatus())) {
                    log.error("EventInfo 저장 실패: {}", request);
                    return;
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
        try {
            log.info("Request URL : {}, OffEventInfoId : {}", url, offEventInfo.getId());
            restTemplate.postForEntity(url, null, Void.class);
            log.info("시동 OFF에 따른 운행일지 작성이 성공적으로 완료되었습니다. event ID : {}", offEventInfo.getId());
        } catch (HttpClientErrorException e) {
            Optional<ErrorResponse> errorResponse = ErrorResponse.parseHttpStatusCodeException(e);

            if (errorResponse.isPresent()) {
                log.warn("운행일지 작성에 실패하였습니다. reason : {}", errorResponse.get().errorReason().reason());
                return;
            }
            log.warn("운행일지 작성과 요청 정보 파싱에 실패하였습니다. responseBody : {}", e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
            log.error("서버 오류로 인해 운행일지 작성 요청에 실패하였습니다. event id : {}", offEventInfo.getId());
        } catch (ResourceAccessException e) {
            log.error("네트워크 오류로 인해 운행일지 작성 요청에 실패하였습니다. event id : {}, url : {}", offEventInfo.getId(), url);
            //NOTE: 5XX 에러 (서버, 네트워크 등 오류시 재시도 여부 결정 필요)
        } catch (Exception e) {
            // FIXME: 무한 요청 방지를 위한 임시 catch
        }
    }

}
