package com.pickcar.dto.command;

import lombok.Builder;

@Builder
public record ReservationReturnCommand(
        //NOTE: Common DTO 모듈로 이동 가능
        Long employeeId,
        Long vehicleId,
        String traceId
) implements DomainCommand {
    @Override
    public String getCommandType() {
        return "RESERVATION_RETURN";
    }

    @Override
    public String getTraceId() {
        return traceId;
    }
}
