package com.pickcar.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    private Long vehicleId;

    @JsonFormat(pattern = "yyyyMMddHHmmss")
    private LocalDateTime occurredAt;

    private Integer cycleCnt;

    private Map<String, Object> cycleInfos;
}
