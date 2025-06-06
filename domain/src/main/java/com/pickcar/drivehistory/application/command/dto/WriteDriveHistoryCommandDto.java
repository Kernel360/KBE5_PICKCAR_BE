package com.pickcar.drivehistory.application.command.dto;

import com.pickcar.emulator.domain.Cycle;
import java.time.LocalDateTime;
import java.util.List;

public record WriteDriveHistoryCommandDto(
        Long reservationId,
        LocalDateTime drivingStartedAt,
        LocalDateTime drivingEndedAt,
        List<Cycle> cycles
) {
}
