package com.pickcar.gpx.application;

import com.pickcar.gpx.dto.CyclePayload;
import com.pickcar.gpx.infrastructure.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {
    private final EmitterRepository emitterRepository;
    private final RedisPublisher redisPublisher;

    public SseEmitter createSseEmitter(@RequestParam Long vehicleId) {
        log.info("vehicleId {}에 대한 emitter 전송 처리 시작", vehicleId);
        SseEmitter emitter = new SseEmitter(0L);
        emitterRepository.save(vehicleId, emitter);

        log.info("✅ SSE emitter 등록 완료: vehicleId={}, emitter={}", vehicleId, emitter);

        emitter.onCompletion(() -> {
            log.info("emitter 연결 제거");
            emitterRepository.remove(vehicleId, emitter);
        });
        emitter.onTimeout(() -> {
            log.info("emitter 시간완료 제거");
            emitterRepository.remove(vehicleId, emitter);
        });
        emitter.onError((e) -> {
            log.warn("SSE 오류 발생: {}", e.getMessage());
            emitterRepository.remove(vehicleId, emitter);
        });

        return emitter;
    }

    public void deleteSseEmitter(@RequestParam Long vehicleId) {
        log.info("vehicleId {}에 대한 emitter 연결 해제 시작", vehicleId);
        emitterRepository.deleteSseEmitter(vehicleId);
    }

    public void pushEvent(@RequestBody CyclePayload payload) {
        log.info("emitter 데이터 전송: {}", payload);
        redisPublisher.publish(payload.getVehicleId(), payload);
    }

    public int processSseConnectionCount() {
        int connectCount = emitterRepository.countEmitters();
        log.info("현재 전체 커넥트 수 : {}", connectCount);
        return connectCount;
    }

}
