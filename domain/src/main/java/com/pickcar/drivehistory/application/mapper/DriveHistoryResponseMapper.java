package com.pickcar.drivehistory.application.mapper;

import com.pickcar.drivehistory.infrastructure.dto.DriveHistoryListProjection;
import com.pickcar.drivehistory.presentation.dto.response.DriveHistoryListResponse;
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
}
