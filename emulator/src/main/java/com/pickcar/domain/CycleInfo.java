package com.pickcar.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CycleInfo {

    @JsonFormat(pattern = "yyyyMMddHHmmss")
    private LocalDateTime second;

    @Enumerated(EnumType.STRING)
    private GpsStatus gps_status;

    private Double latitude;

    private Double longitude;

    private Integer angle;

    private Integer speed;

    private Integer battery;
}
