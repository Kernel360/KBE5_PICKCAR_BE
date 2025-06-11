package com.pickcar.application;

import com.pickcar.mq.config.CycleRabbitMQConfig;
import com.pickcar.mq.dto.CyclePayload;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CycleMessageListener {


    private final CycleInfoService cycleInfoService;

    @RabbitListener(queues = CycleRabbitMQConfig.CYCLE_QUEUE)
    public void cycleMessage(CyclePayload cyclePayload) {
        cycleInfoService.cycle(cyclePayload);
    }
}