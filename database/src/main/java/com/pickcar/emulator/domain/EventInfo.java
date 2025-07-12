package com.pickcar.emulator.domain;

import com.pickcar.dto.EventStatus;
import com.pickcar.dto.GpsStatus;
import com.pickcar.global.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder
@AllArgsConstructor
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventInfo extends BaseEntity {

    private Long vehicleId;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;

    private LocalDateTime engineOnTime;

    private LocalDateTime engineOffTime;

    @Enumerated(EnumType.STRING)
    private GpsStatus gpsStatus;

    private Double latitude;

    private Double longitude;
}
