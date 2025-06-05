package com.pickcar.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pickcar.domain.CycleInfo;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CycleInfoConverter {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public String convertMapToString(Map<String, Object> cycleInfoMap) {
        try {
            return objectMapper.writeValueAsString(cycleInfoMap);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON을 Map으로 변환할 수 없습니다.", e);       //FIXME: fix to custom exception
        }
    }

    public CycleInfo convertRawMapToCycleInfo(Object rawCycleInfoMap) {
        try {
            log.info("convert rawMap to cycleInfo, target : {}", rawCycleInfoMap);
            return objectMapper.convertValue(rawCycleInfoMap, CycleInfo.class);
        } catch (Exception e) {
            //FIXME: 광범위 Exception 사용 x + Custom Exception 변경
            throw new IllegalArgumentException("변환할 수 없습니다", e);
        }
    }

    public CycleInfo convertStringToCycleInfo(String cycleInfoStr) {
        try {
            log.info("convert string to cycleInfo, target : {}", cycleInfoStr);

            //FIXME: 인덱스 한 번 빼는거 필요 <= 그럼 COUNT 필요
            return objectMapper.readValue(cycleInfoStr, CycleInfo.class);
        } catch (Exception e) {
            //FIXME: 광범위 Exception 사용 x + Custom Exception 변경
            throw new IllegalArgumentException("CycleInfo String을 CycleInfo로 변환할 수 없습니다", e);
        }
    }
}
