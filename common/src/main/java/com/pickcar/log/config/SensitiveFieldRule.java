package com.pickcar.log.config;

import java.util.List;

public record SensitiveFieldRule(
        String path,
        List<String> fields
) {
}
