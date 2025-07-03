package com.pickcar.log.filter;

import com.pickcar.log.config.LogConfigProps;
import com.pickcar.log.wrapper.RequestWrapper;
import com.pickcar.log.wrapper.ResponseWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingFilter extends OncePerRequestFilter {

    private final LogConfigProps logConfigProps;

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    private static final List<String> EXCLUDED_PATTERNS = Arrays.asList(
            "/swagger-ui",
            "/actuator/health",
            "/v3/api-docs/**",
            "/api/v1/sse/**"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return EXCLUDED_PATTERNS.stream()
                .anyMatch(pattern -> PATH_MATCHER.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

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

        if(uri.contains("/api/v1/")) {
            String[] split = uri.split("/api/v1/");
            String[] split1 = split[1].split("/");

            if(split1[0] != null) {
                MDC.put("service", split1[0]);
                return;
            }
        }

        MDC.put("service", "unknown");
    }
}
