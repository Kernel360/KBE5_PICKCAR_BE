package com.pickcar.application;

import com.pickcar.dto.EventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventMessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${mq.event.exchange}")
    private String exchange;

    @Value("${mq.event.routing-key}")
    private String routingKey;

    // TODO: publish를 합칠까 고려중
    public void publish(EventPayload eventPayload) {
      
        String traceId = MDC.get("traceId");
      
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, eventPayload, msg -> {
                msg.getMessageProperties().setHeader("traceId", traceId);
                return msg;
            });
            log.info("MQ 전송 성공: exchange={}, routingKey={}, payload={}", exchange, routingKey, eventPayload);
        } catch (Exception e) {
            log.error("MQ 전송 실패: exchange={}, routingKey={}, payload={}", exchange, routingKey, eventPayload);
            // TODO: 실패 시 재처리 고려
        }
    }
}
