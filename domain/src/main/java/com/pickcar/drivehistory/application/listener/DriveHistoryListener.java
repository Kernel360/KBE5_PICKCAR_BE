package com.pickcar.drivehistory.application.listener;

import com.pickcar.constants.GlobalStatic.MDCConstants;
import com.pickcar.drivehistory.application.DriveHistoryService;
import com.pickcar.drivehistory.presentation.dto.payload.DriveHistoryPayload;
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
public class DriveHistoryListener {

    @Value("${mq.drive-history.queue}")
    private String queueName;

    @Value("${logging.module-name}")
    private String moduleName;

    private final DriveHistoryService driveHistoryService;

    @RabbitListener(queues = "${mq.drive-history.queue}")
    public void handleDriveHistoryPayload(DriveHistoryPayload payload,
                                          @Header(MDCConstants.TRACE_ID_HEADER_KEY) String traceId) {
        MDC.put(MDCConstants.TRACE_ID_KEY, traceId);
        MDC.put(MDCConstants.MODULE_NAME_KEY, moduleName);
        MDC.put(MDCConstants.SERVICE_NAME_KEY, queueName);

        log.info("운행일지 작성 요청 수신. payload: {}", payload.toString());
        try {
            driveHistoryService.write(payload);
        } finally {
            MDC.clear();
        }
    }
}
