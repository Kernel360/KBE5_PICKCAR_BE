package com.pickcar.gpx.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickcar.dto.CycleInfoPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;

    // Redis로 데이터를 받으면 자동으로 실행
    public void onMessage(String message) {
        try {

            log.info("Received from Redis: {}", message);

            CycleInfoPayload cyclePayload = objectMapper.convertValue(message, CycleInfoPayload.class);

            // Websocket 채널을 구독하고있는 모든 클라이언트에게 전송 요청
            messagingTemplate.convertAndSend("/topic/cycle-info", cyclePayload);

            log.info("Message sent to WebSocket topic /topic/cycle-info");
        } catch (Exception e) {
            log.error("Error processing message from Redis", e);
        }

    }
}
