package com.pickcar.config;

import com.pickcar.config.interceptor.TraceIdPropagationInterceptor;
import com.pickcar.constants.GlobalStatic.MDCConstants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(TraceIdPropagationInterceptor interceptor) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(interceptor);
        return restTemplate;
    }
}
