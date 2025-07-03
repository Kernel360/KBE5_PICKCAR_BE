package com.pickcar.log.wrapper;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
public class ResponseWrapper extends ContentCachingResponseWrapper {

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    public void loggingResponseAPI() throws IOException {
        String responseBody = this.getContentAsString();
        String statusCode = MDC.get("statusCode");

        if(statusCode != null) {
            if(statusCode.startsWith("4")) {
                log.warn("Response : {}", responseBody);
                return;
            }

            if(statusCode.startsWith("5")) {
                log.error("Response : {}", responseBody);
                return;
            }
        }

        log.info("Response : {}", responseBody);
    }

    private String getContentAsString() throws IOException {
        byte[] content = this.getContentAsByteArray();
        String characterEncoding = this.getCharacterEncoding();

        return new String(content, characterEncoding);
    }
}
