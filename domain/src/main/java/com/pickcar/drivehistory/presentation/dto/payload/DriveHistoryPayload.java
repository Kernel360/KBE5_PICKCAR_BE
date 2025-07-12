package com.pickcar.drivehistory.presentation.dto.payload;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DriveHistoryPayload {
    private Long userId;
    private Long vehicleId;
    private Long offEventInfoId;
    private LocalDateTime engineOnTime;
    private LocalDateTime engineOffTime;
    private Double destLon;
    private Double destLat;
    private String traceId;
}
