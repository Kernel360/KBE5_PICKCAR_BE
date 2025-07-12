package com.pickcar.drivehistory.presentation.dto.response;

import com.pickcar.drivehistory.domain.DriveHistory;
import com.pickcar.emulator.presentation.dto.context.PathContext;
import com.pickcar.reservation.domain.ReservationStatus;
import com.pickcar.reservation.presentation.dto.context.ReservationContext;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;

@Builder
public record DriveHistoryDetailResponse(
        String licensePlate,
        String model,
        String carAge,
        ReservationStatus reservationStatus,
        LocalDateTime drivingStartedAt,
        LocalTime totalDrivingTime,
        Double totalDistance,
        String driverName,
        List<PathContext> paths
) {

    public static DriveHistoryDetailResponse of(DriveHistory history, ReservationContext context, List<PathContext> paths) {
        return DriveHistoryDetailResponse.builder()
                .licensePlate(context.reservedVehicleInfo().getLicensePlate())
                .model(context.reservedVehicleInfo().getModel())
                .carAge(context.reservedVehicleInfo().getCarAge())
                .reservationStatus(context.reservation().getStatus())
                .drivingStartedAt(history.getDrivingStartedAt())
                .totalDrivingTime(history.getTotalDrivingTime())
                .totalDistance(history.getTotalDistance())
                .driverName(context.reservedUserInfo().getName())
                .paths(paths)
                .build();
    }
}
