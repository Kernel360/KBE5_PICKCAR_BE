package com.pickcar.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EventInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long carId;

    private Boolean status;

    private LocalDateTime engineOnTime;

    private LocalDateTime engineOffTime;

    private GpsStatus gpsStatus;

    private Double latitude;

    private Double longitude;

    private Integer angle;

    private Integer speed;

    private Integer total_distance;
}
