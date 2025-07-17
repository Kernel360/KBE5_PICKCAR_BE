package com.pickcar.dailyreport.infrastructure.dto;

import com.pickcar.drivehistory.domain.Region1Depth;

public record DestinationStatProjection(
        Region1Depth destination,
        Long visitCount
) {
}
