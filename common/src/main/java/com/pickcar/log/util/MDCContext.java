package com.pickcar.log.util;

import com.pickcar.constants.GlobalStatic.MDCConstants;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

public class MDCContext {

    private static final Pattern SERVICE_PATTERN = Pattern.compile(MDCConstants.API_PREFIX
            + "/([^/]+)");

    /*
    NOTE: 기본적으로 TraceID는 micrometer-brave를 통해 전파되지만, MQ, RestTemplate 등의 통신은
    MDC 전파가 되지 않음. 그 때문에 MQ 헤더나 Http 헤더에 내용을 담아 전파를 해주어야 함.
     */
    public static void setTraceIdFromHeader(String traceId) {
        if(StringUtils.hasText(traceId)) {
            MDC.put(MDCConstants.TRACE_ID_KEY, traceId);
        }
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