package com.pickcar.config.interceptor;

import com.pickcar.constants.GlobalStatic.MDCConstants;
import java.io.IOException;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class TraceIdPropagationInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        String traceId = MDC.get(MDCConstants.TRACE_ID_KEY);
        if(traceId != null) {
            request.getHeaders().add(MDCConstants.TRACE_ID_HEADER_KEY, traceId);
        }

        return execution.execute(request, body);
    }
}
