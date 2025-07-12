package com.pickcar.emulator.infrastructure;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter
public class CycleIdsConverter implements AttributeConverter<List<Long>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Long> cycleIds) {
        try {
            return mapper.writeValueAsString(cycleIds);
        } catch (Exception e) {
            log.error("CycleId to Column 컨버팅 실패 : {}", cycleIds);
            return "[]";
        }
    }

    @Override
    public List<Long> convertToEntityAttribute(String cycleIdsStr) {
        try {
            return mapper.readValue(cycleIdsStr, new TypeReference<List<Long>>(){});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
