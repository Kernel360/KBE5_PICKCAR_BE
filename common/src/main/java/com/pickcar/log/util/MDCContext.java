package com.pickcar.log.util;

import com.pickcar.constants.GlobalStatic.MDCConstants;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.MDC;

public class MDCContext {

    private static final Pattern SERVICE_PATTERN = Pattern.compile(MDCConstants.API_PREFIX
            + "/([^/]+)");

    public static void setTraceId(String traceId) {
        if (traceId == null || traceId.isEmpty()) {
            MDC.remove(MDCConstants.TRACE_ID_KEY);
            return;
        }
        MDC.put(MDCConstants.TRACE_ID_KEY, traceId);
    }

    public static void setModuleName(String moduleName) {
        MDC.put(MDCConstants.MODULE_NAME_KEY, moduleName != null ? moduleName : "unknown");
    }

    public static void setServiceName(String uri) {
        Matcher matcher = SERVICE_PATTERN.matcher(uri);
        MDC.put(MDCConstants.SERVICE_NAME_KEY, matcher.find() ? matcher.group(1) : "unknown");
    }

    public static void setStatusCode(int statusCode) {
        MDC.put(MDCConstants.STATUS_CODE_KEY, String.valueOf(statusCode));
    }

    public static void clear() {
        MDC.clear();
    }
}