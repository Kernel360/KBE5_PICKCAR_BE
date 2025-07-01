package com.pickcar.filter;

import com.pickcar.config.LogConfigProps;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingFilter extends OncePerRequestFilter {

    private final LogConfigProps logConfigProps;

    private static final Set<String> EXCLUDED_PATHS = Set.of(
            "/swagger-ui",
            "/actuator/health",
            "/v3/api-docs",
            "/api/v1/sse"
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if(EXCLUDED_PATHS.stream().anyMatch(request.getRequestURI()::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        Long startTime = System.currentTimeMillis();

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        RequestWrapper requestWrapper = new RequestWrapper(request, logConfigProps.getSensitiveFieldAsMap());
        ResponseWrapper responseWrapper = new ResponseWrapper(response);

        try {
            keepTraceIdIfExist(request);
            putModuleNameToMDC();
            putServiceNameToMDC(request);

            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            Long duration = System.currentTimeMillis() - startTime;

            MDC.put("duration_ms", String.valueOf(duration));
            MDC.put("statusCode", String.valueOf(response.getStatus()));

            requestWrapper.loggingRequestAPI();
            responseWrapper.loggingResponseAPI();

            // 이 내용이 있어야 다음 클라이언트가 요청을 받을 수 있음
            responseWrapper.copyBodyToResponse();

            MDC.clear();
        }
    }

    private void keepTraceIdIfExist(HttpServletRequest request) {
        String traceId = request.getHeader("X-TraceId");

        if (traceId != null) {
            MDC.put("traceId", traceId);
        }
    }

    private void putModuleNameToMDC() {
        String moduleName = logConfigProps.getModuleName();
        if(moduleName != null) {
            MDC.put("moduleName", moduleName);
            return;
        }

        MDC.put("moduleName", "unknown");
    }

    private void putServiceNameToMDC(HttpServletRequest request) {
        String uri = request.getRequestURI();

        String[] split = uri.split("/api/v1/");
        String[] split1 = split[1].split("/");

        if(split1[0] != null) {
            MDC.put("service", split1[0]);
            return;
        }

        MDC.put("service", "unknown");
    }
}
