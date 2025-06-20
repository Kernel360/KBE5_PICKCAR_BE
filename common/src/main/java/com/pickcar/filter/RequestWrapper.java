package com.pickcar.filter;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Slf4j
public class RequestWrapper extends ContentCachingRequestWrapper {

    public RequestWrapper(HttpServletRequest request) {
        super(request);
    }

    protected void loggingRequestAPI() throws IOException {
        String requestBody = this.getContentAsString();
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
}
