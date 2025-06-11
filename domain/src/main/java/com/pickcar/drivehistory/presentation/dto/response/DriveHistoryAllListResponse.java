package com.pickcar.drivehistory.presentation.dto.response;

import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;

@Builder
public record DriveHistoryAllListResponse(
        Long historyId,
        String licensePlate,
        String driverName,
        LocalDateTime drivingStartedAt,
        LocalDateTime drivingEndedAt,
        LocalTime totalDrivingTime,
        Double totalDistance

        //String currentLocation
) {
}
