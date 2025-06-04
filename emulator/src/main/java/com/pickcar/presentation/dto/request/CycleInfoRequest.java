package com.pickcar.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CycleInfoRequest {

    @JsonProperty("car_id")
    private Long carId;

    @JsonFormat(pattern = "yyyyMMddHHmmss")
    @JsonProperty("occurred_at")
    private LocalDateTime occurredAt;

    @JsonProperty("cycle_cnt")
    private Integer cycleCnt;

    @JsonProperty("cycle_infos")
    private Map<String, Object> cycleInfos;
}
