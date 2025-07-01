package com.pickcar.config;

import java.util.List;

public record SensitiveFieldRule(
        String path,
        List<String> fields
) {
}
