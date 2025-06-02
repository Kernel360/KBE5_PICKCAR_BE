package com.pickcar.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pickcar.domain.GpsStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventInfoRequest {

    private Long carId;

    private Boolean status;

    @JsonFormat(pattern = "yyyyMMddHHmmss")
    private LocalDateTime engineOnTime;

    @JsonFormat(pattern = "yyyyMMddHHmmss")
    private LocalDateTime engineOffTime;

    private GpsStatus gpsStatus;

    private Double latitude;

    private Double longitude;

    private Integer angle;

    private Integer speed;

    private Integer total_distance;
}
