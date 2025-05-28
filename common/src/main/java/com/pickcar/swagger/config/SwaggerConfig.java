package com.pickcar.swagger.config;

import com.pickcar.swagger.annotation.ApiErrorCodeExample;
import com.pickcar.swagger.dto.ExampleHolder;
import com.pickcar.exception.BaseErrorCode;
import com.pickcar.exception.ErrorReason;
import com.pickcar.exception.ErrorResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(info());
    }

    private Info info() {
        return new Info()
                .title("PickCar API")
                .description("차량 관제 시스템, PickCar의 API 문서입니다.")
                .version("1.0");
    }

    @Bean
    public OperationCustomizer customizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            ApiErrorCodeExample apiErrorCodeExample = handlerMethod.getMethodAnnotation(ApiErrorCodeExample.class);

            //ApiErrorCodeExample 어노테이션 단 메소드 적용
            if (apiErrorCodeExample != null) {
                generateErrorCodeResponseExample(operation, apiErrorCodeExample.value());
            }
            return operation;
        };
    }

    /**
     * BaseErrorCode 타입의 이넘값들을 문서화 시킵니다. ExplainError 어노테이션으로 부가설명을 붙일수있습니다. 필드들을 가져와서 예시 에러 객체를 동적으로 생성해서 예시값으로 붙입니다.
     */
    private void generateErrorCodeResponseExample(Operation operation, Class<? extends BaseErrorCode> type) {
        ApiResponses responses = operation.getResponses();

        BaseErrorCode[] errorCodes = type.getEnumConstants();

        Map<Integer, List<ExampleHolder>> statusWithExampleHolders =
                Arrays.stream(errorCodes)
                        .map(
                                baseErrorCode -> {
                                    try {
                                        ErrorReason errorReason = baseErrorCode.getErrorReason();
                                        return ExampleHolder.builder()
                                                .holder(getSwaggerExample(baseErrorCode.getExplainError(), errorReason))
                                                .code(errorReason.status())
                                                .name(errorReason.code())
                                                .build();
                                    } catch (NoSuchFieldException e) {
                                        throw new RuntimeException(e);      //TODO: 잡는 부분을 넣거나, 던지지 말고 다르게 처리
                                    }
                                }).collect(Collectors.groupingBy(ExampleHolder::code));
        addExamplesToResponses(responses, statusWithExampleHolders);
    }

    private Example getSwaggerExample(String value, ErrorReason errorReason) {

        ErrorResponse response = new ErrorResponse(errorReason.status(), errorReason.code(), errorReason.reason());
        Example example = new Example();
        example.description(value);
        example.setValue(response);

        return example;
    }

    private void addExamplesToResponses(ApiResponses responses,
                                        Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {
        statusWithExampleHolders.forEach(
                (status, v) -> {
                    Content content = new Content();
                    MediaType mediaType = new MediaType();
                    ApiResponse apiResponse = new ApiResponse();

                    v.forEach(
                            exampleHolder -> mediaType.addExamples(
                                    exampleHolder.name(), exampleHolder.holder())
                    );

                    content.addMediaType("application/json", mediaType);
                    apiResponse.setContent(content);

                    responses.addApiResponse(status.toString(), apiResponse);

                });
    }
}
