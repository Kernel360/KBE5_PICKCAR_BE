package com.pickcar.gpx.infrastructure;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Component
public class EmitterRepository {
    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter save(Long vehicleId, SseEmitter emitter) {
        emitters.computeIfAbsent(vehicleId, k -> new CopyOnWriteArrayList<>()).add(emitter);
        return emitter;
    }

    public void remove(Long vehicleId, SseEmitter emitter) {
        List<SseEmitter> emitterList = emitters.get(vehicleId);
        if (emitterList != null) {
            emitterList.remove(emitter);
            if (emitterList.isEmpty()) {
                emitters.remove(vehicleId);
            }
        }
    }

    public void deleteSseEmitter(Long vehicleId) {
        List<SseEmitter> emitterList = emitters.get(vehicleId);

        if (emitterList != null) {
            for (SseEmitter emitter : emitterList) {
                emitter.complete();
            }
            emitters.remove(vehicleId);
        }
    }

    public List<SseEmitter> getEmitters(Long vehicleId) {
        return emitters.getOrDefault(vehicleId, List.of());
    }

    public int countEmitters() {
        return emitters.size();
    }

}