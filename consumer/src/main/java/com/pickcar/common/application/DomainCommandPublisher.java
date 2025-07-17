package com.pickcar.common.application;

import com.pickcar.dto.command.DomainCommand;
import com.pickcar.constants.GlobalStatic.MDCConstants;
import com.pickcar.dto.command.DriveHistoryWriteCommand;
import com.pickcar.dto.command.ReservationReturnCommand;
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
public class DomainCommandPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${mq.domain-command.exchange}")
    private String exchange;

    @Value("${mq.domain-command.routing-key}")
    private String routingKey;

    private void publishDomainCommand(DomainCommand command) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, command, message -> {
                message.getMessageProperties().setHeader(MDCConstants.TRACE_ID_HEADER_KEY, command.getTraceId());
                message.getMessageProperties().setHeader("commandType", command.getCommandType());
                return message;
            });
            log.info("도메인 커맨드 publish 완료. command: {}", command);
        } catch (Exception e) {
            log.warn("도메인 커맨드 publish 실패. command: {}, error: {}",
                    command.toString(), e.getMessage());
        }
    }

    public void publishDriveHistory(EventInfo offEventInfo, Long userId) {
        DriveHistoryWriteCommand command = DriveHistoryWriteCommand.builder()
                .userId(userId)
                .vehicleId(offEventInfo.getVehicleId())
                .offEventInfoId(offEventInfo.getId())
                .engineOnTime(offEventInfo.getEngineOnTime())
                .engineOffTime(offEventInfo.getEngineOffTime())
                .destLon(offEventInfo.getLongitude())
                .destLat(offEventInfo.getLatitude())
                .traceId(MDC.get(MDCConstants.TRACE_ID_KEY))
                .build();

        publishDomainCommand(command);
    }

    public void publishReservationReturn(Long employeeId, Long vehicleId) {
        ReservationReturnCommand command = new ReservationReturnCommand(
                employeeId,
                vehicleId,
                MDC.get(MDCConstants.TRACE_ID_KEY)
        );

        publishDomainCommand(command);
    }

}
