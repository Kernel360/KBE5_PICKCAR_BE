package com.pickcar.drivehistory.application;

import com.pickcar.constants.GlobalStatic.MDCConstants;
import com.pickcar.drivehistory.presentation.dto.payload.DriveHistoryPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DriveHistoryListener {

    private final DriveHistoryService driveHistoryService;

    @RabbitListener(queues = "${mq.drive-history.queue}")
    public void handleDriveHistoryPayload(DriveHistoryPayload payload,
                                          @Header(MDCConstants.TRACE_ID_HEADER_KEY) String traceId) {
        MDC.put(MDCConstants.TRACE_ID_KEY, traceId);
        MDC.put(MDCConstants.SERVICE_NAME_KEY, "drivehistory");

        log.info("운행일지 작성 이벤트 수신. offEventInfoId: {}", payload.getOffEventInfoId());

        try {
            driveHistoryService.write(payload);
            log.info("운행일지 작성 완료. offEventInfoId: {}", payload.getOffEventInfoId());
        } catch (Exception e) {
            log.error("운행일지 작성 실패. offEventInfoId: {}, error: {}",
                    payload.getOffEventInfoId(), e.getMessage());
        } finally {
            MDC.clear();
        }
    }


}
