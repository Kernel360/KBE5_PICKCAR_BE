package com.pickcar.drivehistory.presentation.dto.response;

import com.pickcar.drivehistory.domain.DriveHistory;
import com.pickcar.reservation.presentation.dto.context.ReservationContext;
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

    public static DriveHistoryAllListResponse of(DriveHistory history, ReservationContext context) {
        return DriveHistoryAllListResponse.builder()
                .historyId(history.getId())
                .licensePlate(context.reservedVehicleInfo().getLicensePlate())
                .driverName(context.reservedUserInfo().getName())
                .drivingStartedAt(history.getDrivingStartedAt())
                .drivingEndedAt(history.getDrivingEndedAt())
                .totalDrivingTime(history.getTotalDrivingTime())
                .totalDistance(history.getTotalDistance())
                .build();
    }
}
