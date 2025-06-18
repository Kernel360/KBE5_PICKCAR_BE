package com.pickcar.gpx.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickcar.gpx.domain.GpxData;
import com.pickcar.gpx.domain.TrackPoint;
import com.pickcar.gpx.infrastructure.config.GpxWebSocketHandler;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GpxFileService {

    private final GpxWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper;

    @Async("taskExecutor")
    public void streamGpxFile(Resource gpxResource) {
        String fileName = gpxResource.getFilename();
        log.info("{} 파일 스트리밍 시작", fileName);

        // Thread.currentThread().isInterrupted()는 스레드가 중지 신호를 받았는지 확인하는 안전장치입니다.
        while (!Thread.currentThread().isInterrupted()) {
            try (InputStream inputStream = gpxResource.getInputStream()) {
                JAXBContext jaxbContext = JAXBContext.newInstance(GpxData.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                GpxData gpxData = (GpxData) jaxbUnmarshaller.unmarshal(inputStream);

                for (TrackPoint point : gpxData.getTrack().getTrackSegment().getTrackPoints()) {
                    // [추가] 반복문 안에서 스레드 중단 요청이 있었는지 한번 더 확인
                    if (Thread.currentThread().isInterrupted()) {
                        log.info("{} 스트리밍 중단 요청을 확인하여 종료합니다.", fileName);
                        return; // 메소드 종료
                    }

                    Map<String, Object> messagePayload = new HashMap<>();
                    messagePayload.put("id", fileName);
                    messagePayload.put("lat", point.getLat());
                    messagePayload.put("lon", point.getLon());
                    String jsonMessage = objectMapper.writeValueAsString(messagePayload);

                    webSocketHandler.broadcast(jsonMessage);
                    Thread.sleep(1000); // 1초 대기
                }
                log.info("{} 파일의 모든 좌표를 전송 완료. 처음부터 다시 시작합니다.", fileName);

            } catch (Exception e) {
                // InterruptedException 등이 발생하면 스레드의 interrupted 상태가 초기화될 수 있으므로,
                // 다시 중단 신호를 설정하여 while 루프가 안전하게 종료되도록 합니다.
                log.error("{} 파일 처리 중 오류 발생. 스트리밍을 중단합니다.", fileName, e);
                Thread.currentThread().interrupt(); // while 루프 종료를 위해 필수
            }
        }
        // while 루프가 종료되면 아래 로그가 출력됩니다.
        log.info("{} 파일 스트리밍 완전 종료.", fileName);
    }
}
