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
                             @Header(value = "Authorization", required = false) String accessToken) {
        MDC.put(MDCConstants.TRACE_ID_KEY, traceId);
        MDC.put(MDCConstants.MODULE_NAME_KEY, moduleName);
        MDC.put(MDCConstants.SERVICE_NAME_KEY, queueName);

        log.info("RabbitMQ Listener received event: {}", eventPayload.toString());
        try {
            if (EventStatus.ON.equals(eventPayload.getEventStatus())) {
                log.info("EventStatus ON");
                eventInfoService.on(eventPayload);
            } else if (EventStatus.OFF.equals(eventPayload.getEventStatus())) {
                eventInfoService.off(eventPayload);
                log.info("EventStatus OFF");
            } else if (EventStatus.RETURNED.equals(eventPayload.getEventStatus())) {

                if (accessToken == null) {
                    //FIXME: 단순 로그 말고 예외처리로
                    log.warn("반납 요청을 했지만 액세스 토큰이 없어 작업을 진행할 수 없습니다.");
                    return;
                }
                eventInfoService.returned(eventPayload, accessToken);
                log.info("EventStatus RETURNED");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            MDC.clear();
        }
    }
}