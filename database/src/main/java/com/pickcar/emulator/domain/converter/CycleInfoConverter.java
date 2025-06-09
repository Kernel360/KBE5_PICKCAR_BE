package com.pickcar.emulator.domain.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pickcar.emulator.domain.CycleInfo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter
public class CycleInfoConverter implements AttributeConverter<List<CycleInfo>, String> {

    //TODO: Mapper와 Converter 역할 분리 (컨버터는 컨버터만, 매퍼는 매퍼만)

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

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

    @Override
    public String convertToDatabaseColumn(List<CycleInfo> attribute) {
        try{
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("json 값을 cycleInfo로 변환할 수 없습니다.", e);
        }
    }

    @Override
    public List<CycleInfo> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) return Collections.emptyList();
        try {
            JavaType type = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, CycleInfo.class);
            return objectMapper.readValue(dbData, type);
        } catch (IOException e) {
            throw new IllegalArgumentException("JSON deserialization error", e);
        }
    }
}
