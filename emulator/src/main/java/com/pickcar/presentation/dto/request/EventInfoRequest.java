package com.pickcar.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pickcar.emulator.domain.GpsStatus;
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

    @JsonProperty("vehicle_id")
    private Long vehicleId;

    private Boolean status;

    @JsonFormat(pattern = "yyyyMMddHHmmss")
    @JsonProperty("engine_on_time")
    private LocalDateTime engineOnTime;

    @JsonFormat(pattern = "yyyyMMddHHmmss")
    @JsonProperty("engine_off_time")
    private LocalDateTime engineOffTime;

    @JsonProperty("gps_status")
    private GpsStatus gpsStatus;

    private Double latitude;

    private Double longitude;
}
