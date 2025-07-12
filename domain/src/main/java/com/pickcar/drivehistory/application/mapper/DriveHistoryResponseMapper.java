package com.pickcar.drivehistory.application.mapper;

import com.pickcar.drivehistory.infrastructure.dto.DriveHistoryDetailProjection;
import com.pickcar.drivehistory.infrastructure.dto.DriveHistoryListProjection;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryDetailResponse;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryListResponse;
import com.pickcar.emulator.presentation.dto.context.PathContext;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

@Component
public class DriveHistoryResponseMapper {

    public Page<DriveHistoryListResponse> toResponsePage(
            Page<DriveHistoryListProjection> projectionPage) {

        List<DriveHistoryListResponse> responses = projectionPage.getContent()
                .stream()
                .map(DriveHistoryListProjection::toResponse)
                .toList();

        return new PageImpl<>(responses, projectionPage.getPageable(),
                projectionPage.getTotalElements());
    }

    public DriveHistoryDetailResponse toResponseDetail(
            DriveHistoryDetailProjection projection,
            List<PathContext> paths) {
        return DriveHistoryDetailResponse.builder()
                .licensePlate(projection.licensePlate())
                .model(projection.model())
                .carAge(projection.carAge())
                .reservationStatus(projection.reservationStatus())
                .drivingStartedAt(projection.drivingStartedAt())
                .totalDrivingTime(projection.totalDrivingTime())
                .totalDistance(projection.totalDistance())
                .driverName(projection.driverName())
                .destination(projection.destination().name())
                .paths(paths)
                .build();
    }
}
