package com.pickcar.application;

import com.pickcar.mq.dto.CyclePayload;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CycleMessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(CyclePayload cyclePayload) {
        rabbitTemplate.convertAndSend("exchange-cycle", "cycle.key", cyclePayload);
    }
}