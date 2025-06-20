package com.pickcar.filter;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
public class ResponseWrapper extends ContentCachingResponseWrapper {

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    protected void loggingResponseAPI() throws IOException {
        String responseBody = this.getContentAsString();
        log.info("Response : {}", responseBody);
    }

    private String getContentAsString() throws IOException {
        byte[] content = this.getContentAsByteArray();
        String characterEncoding = this.getCharacterEncoding();

        return new String(content, characterEncoding);
    }
}
