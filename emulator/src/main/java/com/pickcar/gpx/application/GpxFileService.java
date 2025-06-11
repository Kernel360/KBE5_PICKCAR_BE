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

        try (InputStream inputStream = gpxResource.getInputStream()) {
            JAXBContext jaxbContext = JAXBContext.newInstance(GpxData.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            GpxData gpxData = (GpxData) jaxbUnmarshaller.unmarshal(inputStream);

            for (TrackPoint point : gpxData.getTrack().getTrackSegment().getTrackPoints()) {
                Map<String, Object> messagePayload = new HashMap<>();
                messagePayload.put("id", fileName);
                messagePayload.put("lat", point.getLat());
                messagePayload.put("lon", point.getLon());
                String jsonMessage = objectMapper.writeValueAsString(messagePayload);

                webSocketHandler.broadcast(jsonMessage);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            log.error("{} 파일 처리 중 오류 발생", fileName, e);
        }
        log.info("{} 파일 스트리밍 종료", fileName);
    }
}
