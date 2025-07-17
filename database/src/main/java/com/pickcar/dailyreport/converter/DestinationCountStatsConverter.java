package com.pickcar.dailyreport.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickcar.dailyreport.domain.DestinationCountStat;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

@Converter
public class DestinationCountStatsConverter implements
        AttributeConverter<List<DestinationCountStat>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<DestinationCountStat> attribute) {
        try {
            return attribute == null ? null : objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert DestinationStats to JSON", e);
        }
    }

    @Override
    public List<DestinationCountStat> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(dbData, new TypeReference<List<DestinationCountStat>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON to DestinationStats", e);
        }
    }
}
