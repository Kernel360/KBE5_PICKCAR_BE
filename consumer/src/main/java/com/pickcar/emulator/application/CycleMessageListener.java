package com.pickcar.emulator.application;

import com.pickcar.constants.GlobalStatic.MDCConstants;
import com.pickcar.emulator.dto.CyclePayload;
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
public class CycleMessageListener {

    @Value("${logging.module-name}")
    private String moduleName;

    @Value("${mq.cycle.queue}")
    private String queueName;

    private final CycleInfoService cycleInfoService;

    @RabbitListener(queues = "${mq.cycle.queue}")
    public void cycleMessage(CyclePayload cyclePayload, @Header(MDCConstants.TRACE_ID_HEADER_KEY) String traceId) {

        MDC.put(MDCConstants.TRACE_ID_KEY, traceId);
        MDC.put(MDCConstants.MODULE_NAME_KEY, moduleName);
        MDC.put(MDCConstants.SERVICE_NAME_KEY, queueName);

        log.info("Received cycle message: {}", cyclePayload);
        try {
            cycleInfoService.cycle(cyclePayload);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            MDC.clear();
        }
    }
}