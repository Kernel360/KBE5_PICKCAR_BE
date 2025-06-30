package com.pickcar.application;

import com.pickcar.dto.EventPayload;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventMessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${mq.event.exchange}")
    private String exchange;

    @Value("${mq.event.routing-key}")
    private String routingKey;

    public void publish(EventPayload eventPayload) {

        String traceId = MDC.get("traceId");

        rabbitTemplate.convertAndSend(exchange, routingKey, eventPayload, msg -> {
            msg.getMessageProperties().setHeader("traceId", traceId);
            return msg;
        });
    }
}
