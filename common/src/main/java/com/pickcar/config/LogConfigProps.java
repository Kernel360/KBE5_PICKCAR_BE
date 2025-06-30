package com.pickcar.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "custom.logging")
public class LogConfigProps {

    private String moduleName;
    private List<SensitiveFieldRule> sensitiveFieldRules;

    public Map<String, List<String>> getSensitiveFieldAsMap() {
        return sensitiveFieldRules.stream()
                .collect(Collectors.toMap(
                        SensitiveFieldRule::path,
                        SensitiveFieldRule::fields
                ));
    }
}
