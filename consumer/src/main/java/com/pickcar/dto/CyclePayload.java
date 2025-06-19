package com.pickcar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CyclePayload implements Serializable {
    @JsonProperty("vehicle_id")
    private Long vehicleId;

    @JsonFormat(pattern = "yyyyMMddHHmmss")
    @JsonProperty("occurred_at")
    private LocalDateTime occurredAt;

    @JsonProperty("cycle_cnt")
    private Integer cycleCnt;

    @JsonProperty("distance")
    private Double distance;

    @JsonProperty("cycle_infos")
    private List<CycleInfoPayload> cycleInfos;
}