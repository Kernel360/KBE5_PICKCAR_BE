package com.pickcar.log.filter;

import com.pickcar.log.config.LogConfigProps;
import com.pickcar.log.util.MDCContext;
import com.pickcar.log.wrapper.RequestWrapper;
import com.pickcar.log.wrapper.ResponseWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        RequestWrapper requestWrapper = new RequestWrapper(request, logConfigProps.getSensitiveFieldAsMap());
        ResponseWrapper responseWrapper = new ResponseWrapper(response);

        try {
            setMDCContext(request);
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            MDCContext.setStatusCode(response.getStatus());

            requestWrapper.loggingRequestAPI();
            responseWrapper.loggingResponseAPI();

            responseWrapper.copyBodyToResponse();

            MDCContext.clear();
        }
    }

    protected void setMDCContext(HttpServletRequest request) {
        MDCContext.setTraceId(request.getHeader("X-TraceId"));
        MDCContext.setModuleName(logConfigProps.getModuleName());
        MDCContext.setServiceName(request.getRequestURI());
    }
}
