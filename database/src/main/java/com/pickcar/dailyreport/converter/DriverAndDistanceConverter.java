package com.pickcar.dailyreport.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickcar.dailyreport.domain.DriverAndDistanceContext;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

@Converter
public class DriverAndDistanceConverter implements AttributeConverter<List<DriverAndDistanceContext>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<DriverAndDistanceContext> attribute) {
        try {
            return attribute == null ? null : objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert Top3Drivers to JSON", e);
        }
    }

    @Override
    public List<DriverAndDistanceContext> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(dbData, new TypeReference<List<DriverAndDistanceContext>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON to Top3Drivers", e);
        }
    }
}
