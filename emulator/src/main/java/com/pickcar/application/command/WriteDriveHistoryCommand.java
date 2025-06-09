package com.pickcar.application.command;

import com.pickcar.application.CycleService;
import com.pickcar.emulator.domain.Cycle;
import com.pickcar.emulator.domain.EventInfo;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class WriteDriveHistoryCommand {

    private final CycleService cycleService;
    private final RestTemplate restTemplate = new RestTemplate();

    public void execute(EventInfo offEventInfo) {
        HttpHeaders headers = new HttpHeaders();
        WriteDriveHistoryRequest requestBody = new WriteDriveHistoryRequest(offEventInfo,
                getTargetDistances(offEventInfo));
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            restTemplate.postForEntity(
                    "http://localhost:8080/api/v1/history",         //FIXME
                    new HttpEntity<>(requestBody, headers),
                    Void.class
            );
        } catch (HttpServerErrorException e) {
            log.warn("운행일지 작성에 실패하였습니다, occurred by => {}", requestBody.vehicleId);
            throw new IllegalArgumentException("[ERROR] 운행일지 작성에 실패하였습니다.");
        }
    }

    private List<Double> getTargetDistances(EventInfo offEventInfo) {
        return cycleService.getCyclesByOffEventInfo(offEventInfo).stream()
                .map(Cycle::getDistance).toList();
    }

    private record WriteDriveHistoryRequest(
            Long vehicleId,
            LocalDateTime engineOnTime,
            LocalDateTime engineOffTime,
            List<Double> distances
    ) {
        private WriteDriveHistoryRequest(EventInfo offEventInfo, List<Double> distances) {
            this(
                    offEventInfo.getVehicleId(),
                    offEventInfo.getEngineOnTime(),
                    offEventInfo.getEngineOffTime(),
                    distances
            );
        }
    }
}
