//package com.pickcar.dto.request;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.pickcar.domain.CycleInfo;
//import java.time.LocalDateTime;
//import java.util.List;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Getter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class CycleInfoRequest {
//
//    @JsonProperty("vehicle_id")
//    private Long vehicleId;
//
//    @JsonFormat(pattern = "yyyyMMddHHmmss")
//    @JsonProperty("occurred_at")
//    private LocalDateTime occurredAt;
//
//    @JsonProperty("cycle_cnt")
//    private Integer cycleCnt;
//
//    @JsonProperty("distance")
//    private Integer distance;
//
//    @JsonProperty("cycle_infos")
//    private List<CycleInfo> cycleInfos;
//}
