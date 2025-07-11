package com.pickcar.presentation.dto.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pickcar.exception.ErrorReason;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpStatusCodeException;

@Slf4j
public record ErrorResponse(
        ResponseInfo responseInfo,
        ErrorReason errorReason
) {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public ErrorResponse(Integer statusCode, String errorCode, String reason) {
        this(ResponseInfo.error(statusCode), new ErrorReason(errorCode, reason));
    }

    public ErrorResponse(Integer statusCode, ErrorReason errorReason) {
        this(ResponseInfo.error(statusCode), errorReason);
    }

    public static Optional<ErrorResponse> parseHttpStatusCodeException(HttpStatusCodeException exception) {
        String responseBody = exception.getResponseBodyAsString();

        try {
            ErrorResponse errorResponse = OBJECT_MAPPER.readValue(responseBody, ErrorResponse.class);
            return Optional.of(errorResponse);
        } catch (JsonProcessingException e) {
            log.error("ERROR Response 파싱에 실패하였습니다. statusCode: {}, body: {}",
                    exception.getStatusCode(), responseBody);

            return Optional.empty();
        }
    }
}
