package com.pickcar.log.wrapper;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.slf4j.event.Level;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
public class ResponseWrapper extends ContentCachingResponseWrapper {

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    public void loggingResponseAPI() throws IOException {
        String responseBody = this.getContentAsString();
        String statusCode = MDC.get("statusCode");

        Level logLevel = determineLogLevel(statusCode);
        log.atLevel(logLevel).log("Response : {}", responseBody);
    }

    private String getContentAsString() throws IOException {
        byte[] content = this.getContentAsByteArray();
        String characterEncoding = this.getCharacterEncoding();

        return new String(content, characterEncoding);
    }

    private Level determineLogLevel(String statusCode) {

        int parsedStatusCode;

        try {
            parsedStatusCode = Integer.parseInt(statusCode);
        } catch (NumberFormatException e) {
            return Level.INFO;
        }

        if (parsedStatusCode >= 500) return Level.ERROR;
        if(parsedStatusCode >= 400) return Level.WARN;
        return Level.INFO;
    }
}
