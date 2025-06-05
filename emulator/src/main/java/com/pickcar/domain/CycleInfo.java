package com.pickcar.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CycleInfo {

    @JsonFormat(pattern = "yyyyMMddHHmmss")
    LocalDateTime second;

    @JsonProperty("gps_status")
    GpsStatus gpsStatus;

    Double latitude;

    Double longitude;

    Integer angle;

    Integer speed;

    Integer battery;
}
