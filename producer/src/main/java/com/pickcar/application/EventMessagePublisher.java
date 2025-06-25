package com.pickcar.application;

import com.pickcar.dto.EventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventMessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${mq.event.exchange}")
    private String exchange;

    @Value("${mq.event.routing-key}")
    private String routingKey;

    public void publish(EventPayload payload) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, payload);
            log.info("MQ 전송 성공: exchange={}, routingKey={}, payload={}", exchange, routingKey, payload);
        } catch (Exception e) {
            log.error("MQ 전송 실패: exchange={}, routingKey={}, payload={}", exchange, routingKey, payload);
            // TODO: 실패 시 Fallback 고려
        }
    }
}
