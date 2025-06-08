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

    private final GpxFileService gpxTaskService;
    private final AtomicBoolean isStreaming = new AtomicBoolean(false);

    public GpxService(GpxFileService gpxTaskService) {
        this.gpxTaskService = gpxTaskService;
    }


    public String startStreaming() throws IOException {
        if (isStreaming.compareAndSet(false, true)) {
            log.info("---스트리밍 시작---");

            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:gpx/**/*.gpx");

            if (resources.length == 0) {
                log.warn("resources/gpx 폴더에 처리할 GPX 파일 없음");
                isStreaming.set(false);
                return "처리할 GPX 파일이 없음";
            }

            for (Resource resource : resources) {
                gpxTaskService.streamGpxFile(resource);
            }
            return "---GPX 스트리밍이 성공적으로 시작---";
        } else {
            log.info("---이미 GPX 스트리밍이 실행 중---");
            return "---이미 스트리밍이 실행 중---";
        }
    }
}
