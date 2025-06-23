package com.pickcar.application;

import com.pickcar.dto.CyclePayload;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CycleMessageListener {

    private final CycleInfoService cycleInfoService;
    private final RedisPublisherService redisPublisherService;

    @Value("${redis.channel.cycle-info}")
    private String cycleInfoChannel;

    @RabbitListener(queues = "${mq.cycle.queue}")
    public void cycleMessage(CyclePayload cyclePayload) {
        cycleInfoService.cycle(cyclePayload);

        redisPublisherService.publish(cycleInfoChannel, cyclePayload);
    }
}