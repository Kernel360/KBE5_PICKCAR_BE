package com.pickcar.application;

import com.pickcar.mq.config.EventRabbitMQConfig;
import com.pickcar.mq.dto.EventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventMessageListener {

    private final EventInfoService eventInfoService;

    @RabbitListener(queues = EventRabbitMQConfig.EVENT_QUEUE)
    public void cycleMessage(EventPayload eventPayload) {
        if (eventPayload.getStatus()) {
            eventInfoService.on(eventPayload);
        } else {
            eventInfoService.off(eventPayload);
        }
    }
}