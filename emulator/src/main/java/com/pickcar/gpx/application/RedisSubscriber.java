package com.pickcar.gpx.application;

import com.pickcar.gpx.infrastructure.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final EmitterRepository emitterRepository;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
        String payload = new String(message.getBody(), StandardCharsets.UTF_8);

        try {
            Long vehicleId = Long.valueOf(channel.replace("vehicle-cycle-", ""));
            log.info("📨 Redis 메시지 수신: channel={}, vehicleId={}, payload.length={}", channel, vehicleId, payload.length());
            emitterRepository.send(vehicleId, payload);
        } catch (Exception e) {
            System.out.println("🚨 RedisSubscriber 오류: " + e.getMessage());
        }
    }
}
