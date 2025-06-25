package com.pickcar.filter;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Slf4j
public class RequestWrapper extends ContentCachingRequestWrapper {

    private final Map<String, List<String>> sensitiveFields;

    public RequestWrapper(HttpServletRequest request, Map<String, List<String>> sensitiveFields) {
        super(request);
        this.sensitiveFields = sensitiveFields;
    }

    protected void loggingRequestAPI() throws IOException {
        String requestBody = getRequestContent();
        String queryString = this.getQueryString();
        StringBuilder uriBuilder = new StringBuilder(this.getRequestURI());
        if (queryString != null) {
            uriBuilder.append("?").append(queryString);
        }

        log.info("Request : {} URI : {} Content-Type=[{}] Payload=[{}]",
                this.getMethod(),
                uriBuilder,
                this.getContentType(),
                requestBody
        );
    }

    private String getRequestContent() {
        String requestBody = this.getContentAsString();
        String requestURI = this.getRequestURI();

        if (!sensitiveFields.containsKey(requestURI)) {
            return requestBody;
        }

        return maskingPayload(requestBody, sensitiveFields.get(requestURI));
    }

    private String maskingPayload(String requestBody, List<String> sensitiveFields) {
        for (String field : sensitiveFields) {
            requestBody = requestBody.replaceAll(
                    "\"" + field + "\"\\s*:\\s*\"[^\"]*\"",
                    "\"" + field + "\":\"****\""
            );
        }

        return requestBody;
    }
}
