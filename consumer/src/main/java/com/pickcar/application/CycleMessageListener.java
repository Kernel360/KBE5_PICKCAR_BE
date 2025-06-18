package com.pickcar.application;

import com.pickcar.dto.CyclePayload;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CycleMessageListener {

    private final CycleInfoService cycleInfoService;

    @RabbitListener(queues = "${mq.cycle.queue}")
    public void cycleMessage(CyclePayload cyclePayload) {
        cycleInfoService.cycle(cyclePayload);
    }
}