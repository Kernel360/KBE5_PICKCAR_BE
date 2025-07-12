package com.pickcar.drivehistory.presentation.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriveHistoryPayload {
    private Long offEventInfoId;
    private String vehicleId;
    private LocalDateTime engineOnTime;
    private LocalDateTime engineOffTime;
    private String traceId;
}
