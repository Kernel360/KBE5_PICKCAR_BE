package com.pickcar.application;

import com.pickcar.dto.CyclePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CycleMessageListener {

    private final CycleInfoService cycleInfoService;

    @RabbitListener(queues = "${mq.cycle.queue}")
    public void cycleMessage(CyclePayload cyclePayload) {

        log.info("Received cycle message: {}", cyclePayload);
        try {
            cycleInfoService.cycle(cyclePayload);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}