package com.pickcar.emulator.domain;

import com.pickcar.emulator.domain.converter.CycleInfoConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "cycles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long vehicleId;

    private LocalDateTime occurredAt;

    private Integer cycleCnt;

    private Double distance;

    @Convert(converter = CycleInfoConverter.class)
    @Column(columnDefinition = "text")
    private List<CycleInfo> cycleInfos;
}
