package com.pickcar.drivehistory.presentation.dto.request;

import java.time.LocalDateTime;

public record DriveHistoryFilterRequest(
        LocalDateTime from,
        LocalDateTime to,
        String driverName,
        Integer page
) {
}
