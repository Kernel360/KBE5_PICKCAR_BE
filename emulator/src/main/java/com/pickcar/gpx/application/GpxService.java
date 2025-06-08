package com.pickcar.gpx.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickcar.gpx.domain.GpxData;
import com.pickcar.gpx.domain.TrackPoint;
import com.pickcar.gpx.infrastructure.config.GpxWebSocketHandler;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class GpxService {

    private final GpxWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper;
    private final AtomicBoolean isStreaming = new AtomicBoolean(false);

    public GpxService(GpxWebSocketHandler webSocketHandler, ObjectMapper objectMapper) {
        this.webSocketHandler = webSocketHandler;
        this.objectMapper = objectMapper;
    }

    public String startStreaming() throws IOException {
        if (isStreaming.compareAndSet(false, true)) {
            log.info("---스트리밍 시작---");

            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:gpx/*.gpx");

            if (resources.length == 0) {
                log.warn("resources/gpx 폴더에 처리할 GPX 파일 없음");
                isStreaming.set(false);
                return "처리할 GPX 파일이 없음";
            }

            for (Resource resource : resources) {
                this.streamGpxFile(resource);
            }
            return "---GPX 스트리밍이 성공적으로 시작---";
        } else {
            log.info("---이미 GPX 스트리밍이 실행 중---");
            return "---이미 스트리밍이 실행 중---";
        }
    }

    @Async("taskExecutor")
    public void streamGpxFile(Resource gpxResource) {
        String fileName = gpxResource.getFilename();
        log.info("---{} 파일 스트리밍 시작---", fileName);

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
            log.error("---{} 파일 처리 중 오류 발생---", fileName, e);
        }
        log.info("---{} 파일 스트리밍 종료---", fileName);
    }
}
