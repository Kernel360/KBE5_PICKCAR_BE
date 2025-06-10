package com.pickcar.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pickcar.emulator.domain.CycleInfo;
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
public class CycleStoreRequest {

    @JsonProperty("vehicle_id")
    private Long vehicleId;

    @JsonFormat(pattern = "yyyyMMddHHmmss")
    @JsonProperty("occurred_at")
    private LocalDateTime occurredAt;

    @JsonProperty("cycle_cnt")
    private Integer cycleCnt;

    @JsonProperty("cycle_infos")
    private List<CycleInfo> cycleInfos;
}
