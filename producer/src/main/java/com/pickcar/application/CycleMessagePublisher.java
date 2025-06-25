package com.pickcar.application;

import com.pickcar.dto.CyclePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CycleMessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${mq.cycle.exchange}")
    private String exchange;

    @Value("${mq.cycle.routing-key}")
    private String routingKey;

    public void publish(CyclePayload Payload) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, Payload);
            log.info("MQ 전송 성공: exchange={}, routingKey={}, payload={}", exchange, routingKey, Payload);
        } catch (Exception e) {
            log.error("MQ 전송 실패: exchange={}, routingKey={}, payload={}", exchange, routingKey, Payload);
            // TODO: 실패 시 Fallback 고려
        }
    }
}