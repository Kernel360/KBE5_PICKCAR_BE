package com.pickcar.application;

import com.pickcar.dto.CyclePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CycleMessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${mq.cycle.exchange}")
    private String exchange;

    @Bean
    public TopicExchange cycleExchange() {
        return new TopicExchange(exchange);
    }

    @Value("${mq.cycle.routing-key}")
    private String routingKey;

    // TODO: publish를 합칠까 고려중
    public void publish(CyclePayload Payload) {
        String key = String.format("%s.%s", routingKey, Payload.getVehicleId());
        try {
            rabbitTemplate.convertAndSend(exchange, key, Payload);
            log.info("MQ 전송 성공: exchange={}, routingKey={}, payload={}", exchange, key, Payload);
        } catch (Exception e) {
            log.error("MQ 전송 실패: exchange={}, routingKey={}, payload={}", exchange, key, Payload);
            // TODO: 실패 시 재처리 고려
        }
    }
}