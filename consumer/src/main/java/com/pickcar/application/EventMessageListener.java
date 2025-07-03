package com.pickcar.application;

import com.pickcar.dto.EventPayload;
import com.pickcar.dto.EventStatus;
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

    @Value("${logging.module-name}")
    private String moduleName;

    @Value("${mq.event.queue}")
    private String queueName;

    private final EventInfoService eventInfoService;

    @RabbitListener(queues = "${mq.event.queue}")
    public void eventMessage(EventPayload eventPayload, @Header("traceId") String traceId) {
        MDC.put("traceId", traceId);
        MDC.put("moduleName", moduleName);
        MDC.put("service", queueName);

        log.info("RabbitMQ Listener received event: {}", eventPayload.toString());
        try {
            if (EventStatus.ON.equals(eventPayload.getEventStatus())) {
                log.info("EventStatus ON");
                eventInfoService.on(eventPayload);
            } else if (EventStatus.OFF.equals(eventPayload.getEventStatus())) {
                eventInfoService.off(eventPayload);
                log.info("EventStatus OFF");
            } else if (EventStatus.RETURNED.equals(eventPayload.getEventStatus())) {
                eventInfoService.returned(eventPayload);
                log.info("EventStatus RETURNED");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            MDC.clear();
        }
    }
}