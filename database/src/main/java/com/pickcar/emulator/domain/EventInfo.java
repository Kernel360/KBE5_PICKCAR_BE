package com.pickcar.emulator.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EventInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long vehicleId;

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
