package com.pickcar.gpx.application;

import com.pickcar.gpx.infrastructure.EmitterRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
            log.info("Redis 메시지 수신: channel={}, vehicleId={}, payload.length={}", channel, vehicleId, payload.length());

            emitterRepository.getEmitters(vehicleId).forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event()
                            .name("vehicle-cycle")
                            .data(payload));
                    log.info("SSE 전송 성공: vehicleId={}, emitter={}", vehicleId, emitter);
                } catch (IOException | IllegalStateException e) {
                    log.warn("SSE 전송 실패: vehicleId={}, error={}", vehicleId, e.getMessage());
                    emitter.completeWithError(e);
                    emitterRepository.remove(vehicleId, emitter);
                }
            });
        } catch (Exception e) {
            log.error("RedisSubscriber 오류 발생 - channel={}, payload={}", channel, payload);
        }
    }
}
