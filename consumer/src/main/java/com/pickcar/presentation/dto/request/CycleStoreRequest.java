package com.pickcar.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    private Long vehicleId;

    @JsonFormat(pattern = "yyyyMMddHHmmss")
    private LocalDateTime occurredAt;

    private Integer cycleCnt;

    private List<CycleInfo> cycleInfos;
}
