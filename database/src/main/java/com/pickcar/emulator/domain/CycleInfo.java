package com.pickcar.emulator.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CycleInfo {

    @JsonFormat(pattern = "yyyyMMddHHmmss")
    private LocalDateTime second;

    @JsonProperty("gps_status")
    @Enumerated(EnumType.STRING)
    private GpsStatus gpsStatus;

    private Double latitude;

    private Double longitude;

    private Integer angle;

    private Integer speed;

    private Integer battery;
}
