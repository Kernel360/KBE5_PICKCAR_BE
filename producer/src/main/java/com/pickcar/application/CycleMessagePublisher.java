package com.pickcar.application;

import com.pickcar.dto.CyclePayload;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CycleMessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${mq.cycle.exchange}")
    private String exchange;

    @Value("${mq.cycle.routing-key}")
    private String routingKey;

    public void publish(CyclePayload cyclePayload) {
        rabbitTemplate.convertAndSend(exchange, routingKey, cyclePayload);
    }
}