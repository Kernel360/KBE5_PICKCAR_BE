package com.pickcar.application;

import com.pickcar.dto.EventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventMessageListener {

    @Value("${custom.logging.moduleName}")
    private String moduleName;

    @Value("${mq.event.queue}")
    private String queueName;

    private final EventInfoService eventInfoService;

    @RabbitListener(queues = "${mq.event.queue}")
    public void cycleMessage(EventPayload eventPayload, @Header("traceId") String traceId) {
        MDC.put("traceId", traceId);
        MDC.put("moduleName", moduleName);
        MDC.put("service", queueName);

        log.info("RabbitMQ Listener received event: {}", eventPayload.toString());
        try {
            if (eventPayload.getStatus()) {
                eventInfoService.on(eventPayload);
            } else {
                eventInfoService.off(eventPayload);
            }
        } finally {
            MDC.clear();
        }
    }
}