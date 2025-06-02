package com.pickcar.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmulatorRequest {
    private Long carId;
    private String mdn;
    private String terminalId;
    private String manufactureId;
    private String packetVersion;
    private String deviceId;
}
