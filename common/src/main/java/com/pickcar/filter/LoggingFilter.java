package com.pickcar.filter;

import com.pickcar.config.LogConfigProps;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingFilter extends OncePerRequestFilter {

    private final LogConfigProps logConfigProps;

//    @Value("${custom.logging.moduleName}")
//    private String moduleName;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        keepTraceIdIfExist(request);
        putModuleNameToMDC();

        RequestWrapper requestWrapper = new RequestWrapper(request);
        ResponseWrapper responseWrapper = new ResponseWrapper(response);

        filterChain.doFilter(requestWrapper, responseWrapper);

        requestWrapper.loggingRequestAPI();
        responseWrapper.loggingResponseAPI();

        // 이 내용이 있어야 다음 클라이언트가 요청을 받을 수 있음
        responseWrapper.copyBodyToResponse();

        MDC.clear();
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
}
