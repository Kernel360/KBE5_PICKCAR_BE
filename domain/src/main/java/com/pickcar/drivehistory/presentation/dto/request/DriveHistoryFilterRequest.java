package com.pickcar.drivehistory.presentation.dto.request;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriveHistoryFilterRequest {
    private LocalDateTime from;
    private LocalDateTime to;
    private String driverName;
    private Integer page;

    public DriveHistoryFilterRequest() {
    }
}
