package com.pickcar.emulator.application;

import com.pickcar.constants.GlobalStatic.MDCConstants;
import com.pickcar.emulator.dto.EventPayload;
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
    public void eventMessage(EventPayload eventPayload, @Header(MDCConstants.TRACE_ID_HEADER_KEY) String traceId,
                             @Header(value = "userId", required = false) Long userId) {
        MDC.put(MDCConstants.TRACE_ID_KEY, traceId);
        MDC.put(MDCConstants.MODULE_NAME_KEY, moduleName);
        MDC.put(MDCConstants.SERVICE_NAME_KEY, queueName);

        log.info("RabbitMQ Listener received event: {}", eventPayload.toString());      // NOTE: 추적되지 않으므로 로그 O
        try {
            if (EventStatus.ON.equals(eventPayload.getEventStatus())) {
                log.info("EventStatus ON");
                eventInfoService.on(eventPayload);
            } else if (EventStatus.OFF.equals(eventPayload.getEventStatus())) {
                eventInfoService.off(eventPayload, userId);
                log.info("EventStatus OFF");
            } else if (EventStatus.RETURNED.equals(eventPayload.getEventStatus())) {
                eventInfoService.returned(eventPayload, userId);
                log.info("EventStatus RETURNED");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            MDC.clear();
        }
    }
}