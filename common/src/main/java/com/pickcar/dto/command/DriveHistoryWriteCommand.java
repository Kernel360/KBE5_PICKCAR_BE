package com.pickcar.dto.command;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record DriveHistoryWriteCommand (
        Long userId,
        Long vehicleId,
        Long offEventInfoId,
        LocalDateTime engineOnTime,
        LocalDateTime engineOffTime,
        Double destLon,
        Double destLat,
        String traceId
) implements DomainCommand {
    @Override
    public String getCommandType() {
        return "DRIVE_HISTORY_WRITE";
    }

    @Override
    public String getTraceId() {
        return traceId;
    }
}
