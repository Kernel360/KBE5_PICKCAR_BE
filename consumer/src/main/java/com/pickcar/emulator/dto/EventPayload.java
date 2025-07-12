package com.pickcar.emulator.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pickcar.dto.EventStatus;
import com.pickcar.dto.GpsStatus;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventPayload implements Serializable {
    @JsonProperty("vehicle_id")
    private Long vehicleId;

    @JsonProperty("event_status")
    private EventStatus eventStatus;

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
