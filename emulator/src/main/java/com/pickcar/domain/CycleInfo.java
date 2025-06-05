package com.pickcar.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CycleInfo {

    @JsonFormat(pattern = "yyyyMMddHHmmss")
    LocalDateTime second;

    @JsonProperty("gps_status")
    String gpsStatus;

    Double longitude;

    Double latitude;

    Integer angle;

    Integer speed;

    Integer battery;
}
