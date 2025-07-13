package com.pickcar.global.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickcar.constants.GlobalStatic.MDCConstants;
import com.pickcar.drivehistory.application.service.DriveHistoryService;
import com.pickcar.dto.command.DomainCommand;
import com.pickcar.dto.command.DriveHistoryWriteCommand;
import com.pickcar.dto.command.ReservationReturnCommand;
import com.pickcar.reservation.application.ReservationService;
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
public class DomainCommandListener {

    @Value("${mq.domain-command.queue}")
    private String queueName;

    @Value("${logging.module-name}")
    private String moduleName;

    private final DriveHistoryService driveHistoryService;
    private final ReservationService reservationService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "${mq.domain-command.queue}")
    public void handleDriveHistoryPayload(DomainCommand command,
                                          @Header(MDCConstants.TRACE_ID_HEADER_KEY) String traceId,
                                          @Header("commandType") String commandType) {
        MDC.put(MDCConstants.TRACE_ID_KEY, traceId);
        MDC.put(MDCConstants.MODULE_NAME_KEY, moduleName);
        MDC.put(MDCConstants.SERVICE_NAME_KEY, queueName);

        try {
            switch (commandType) {
                case "DRIVE_HISTORY_WRITE" -> handleDriveHistoryWrite(command);
                case "RESERVATION_RETURN" -> handleReservationReturn(command);
            }
        } catch (Exception e) {
            log.error("도메인 커맨드 처리 실패 : type = {}, error = {}", commandType, e.getMessage());
            // throw e
        } finally {
            MDC.clear();
        }
    }

    private void handleDriveHistoryWrite(DomainCommand rawCommand) {
        com.pickcar.dto.command.DriveHistoryWriteCommand command = objectMapper.convertValue(rawCommand, DriveHistoryWriteCommand.class);

        log.info("운행일지 작성 커맨드 시작. command = {}", command.toString());
        driveHistoryService.write(command);
    }

    private void handleReservationReturn(DomainCommand rawCommand) {
        ReservationReturnCommand command = objectMapper.convertValue(rawCommand, ReservationReturnCommand.class);

        log.info("차량 반납 커맨드 시작. command = {}", command.toString());
        reservationService.processReturn(command);
    }

}
