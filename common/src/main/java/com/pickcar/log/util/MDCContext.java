package com.pickcar.log.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.MDC;

public class MDCContext {

    private static final String API_PREFIX = "/api/v1";

    public static void setTraceId(String traceId) {
        if (traceId == null || traceId.isEmpty()) {
            MDC.remove("traceId");
            return;
        }
        MDC.put("traceId", traceId);
    }

    public static void setModuleName(String moduleName) {
        MDC.put("moduleName", moduleName != null ? moduleName : "unknown");
    }

    public static void setServiceName(String uri) {
        Pattern pattern = Pattern.compile(API_PREFIX + "/([^/]+)");
        Matcher matcher = pattern.matcher(uri);
        MDC.put("service", matcher.find() ? matcher.group(1) : "unknown");
    }

    public static void setStatusCode(int statusCode) {
        MDC.put("statusCode", String.valueOf(statusCode));
    }

    public static void clear() {
        MDC.clear();
    }

}
