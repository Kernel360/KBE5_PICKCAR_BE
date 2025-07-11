package com.pickcar.log.wrapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Slf4j
public class RequestWrapper extends ContentCachingRequestWrapper {

    private final Map<String, List<String>> sensitiveFields;

    public RequestWrapper(HttpServletRequest request, Map<String, List<String>> sensitiveFields) {
        super(request);
        this.sensitiveFields = sensitiveFields;
    }

    public void loggingRequestAPI() throws IOException {
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
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(requestBody);

            if(root.isObject()) {
                ObjectNode objectNode = (ObjectNode) root;
                sensitiveFields.forEach(field -> {
                    if(objectNode.has(field)) {
                        objectNode.put(field, "*".repeat(objectNode.get(field).asText().length()));
                    }
                });
            }

            return mapper.writeValueAsString(root);
        } catch (Exception e) {
            log.warn("로그 마스킹에 실패하였습니다 : {}", MDC.get("traceId"));
            return "**********";
        }
    }
}
