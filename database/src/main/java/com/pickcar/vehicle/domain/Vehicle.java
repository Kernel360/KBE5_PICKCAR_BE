package com.pickcar.vehicle.domain;

import com.pickcar.global.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@Table(name = "vehicles")
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vehicle extends BaseEntity {

    @Embedded
    private VehicleInfo info;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    @Column(nullable = false)
    private Boolean hasGps;

    @Column(nullable = false)
    private Boolean isRented;

    @Column(nullable = false)
    private Boolean isActive;       //TODO: 자동차에 애뮬레이터 상태를 저장하는게 올바른지에 대한 고민 필요

    public Vehicle(VehicleInfo info, Boolean hasGps) {
        this.info = info;
        this.hasGps = hasGps;
        this.status = VehicleStatus.OPERABLE;
        this.isRented = false;
        this.isActive = true;
    }

    public void changeStatus(VehicleStatus status) {
        this.status = status;
    }
}

