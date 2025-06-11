package com.pickcar.mq.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pickcar.mq.enumType.GpsStatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;

public class CycleInfoPayload {

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
