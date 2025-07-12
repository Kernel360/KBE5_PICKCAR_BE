package com.pickcar.drivehistory.application;

import com.pickcar.constants.GlobalStatic.MDCConstants;
import com.pickcar.drivehistory.presentation.dto.DriveHistoryPayload;
import com.pickcar.drivehistory.presentation.dto.DriveHistoryPayload.DriveHistoryPayloadBuilder;
import com.pickcar.emulator.domain.EventInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DriveHistoryPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${mq.drive-history.exchange}")
    private String exchange;

    @Value("${mq.drive-history.routing-key}")
    private String routingKey;

    private void publishDriveHistoryPayload(DriveHistoryPayload payload) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, payload, message -> {
                message.getMessageProperties().setHeader(MDCConstants.TRACE_ID_HEADER_KEY, payload.getTraceId());
                return message;
            });
            log.info("운행일지 작성 이벤트 발행 완료. offEventInfoId: {}", payload.getOffEventInfoId());
        } catch (Exception e) {
            log.warn("운행일지 작성 이벤트 발행 실패. offEventInfoId: {}, error: {}",
                    payload.getOffEventInfoId(), e.getMessage());
        }
    }

    public void publishDriveHistory(EventInfo offEventInfo) {

        DriveHistoryPayload payload = DriveHistoryPayload.builder()
                .offEventInfoId(offEventInfo.getId())
                .vehicleId(offEventInfo.getVehicleId())
                .engineOnTime(offEventInfo.getEngineOnTime())
                .engineOffTime(offEventInfo.getEngineOffTime())
                .traceId(MDC.get(MDCConstants.TRACE_ID_KEY))
                .build();

        publishDriveHistoryPayload(payload);
    }

}
