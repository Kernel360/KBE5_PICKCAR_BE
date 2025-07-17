package com.pickcar.drivehistory.infrastructure.dto;

import com.pickcar.drivehistory.domain.Region1Depth;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryListResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record DriveHistoryListProjection(
        Long driveHistoryId,
        LocalDateTime drivingStartedAt,
        LocalDateTime drivingEndedAt,
        Double totalDistance,
        LocalTime totalDrivingTime,
        Region1Depth destination,
        Long reservationId,
        String driverName,
        String vehicleLicensePlate
) {
    public DriveHistoryListResponse toResponse() {
        return DriveHistoryListResponse.builder()
                .historyId(driveHistoryId)
                .drivingStartedAt(drivingStartedAt)
                .drivingEndedAt(drivingEndedAt)
                .totalDistance(totalDistance)
                .totalDrivingTime(totalDrivingTime)
                .destination(destination.name())
                .reservationId(reservationId)
                .driverName(driverName)
                .licensePlate(vehicleLicensePlate)
                .build();
    }
}
