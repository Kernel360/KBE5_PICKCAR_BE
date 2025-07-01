package com.pickcar.gpx.application;

import com.pickcar.gpx.dto.CyclePayload;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(Long vehicleId, CyclePayload payload) {
        redisTemplate.convertAndSend("vehicle-cycle-" + vehicleId, payload);
    }
}
