package com.pickcar.application;

import com.pickcar.mq.dto.EventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventMessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(EventPayload eventPayload) {
        rabbitTemplate.convertAndSend("exchange-event", "event.key", eventPayload);
    }
}
