package com.pickcar.common.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainCommandMessagingConfig {

    @Value("${mq.domain-command.queue}")
    private String domainCommandQueue;

    @Value("${mq.domain-command.exchange}")
    private String domainCommandExchange;

    @Value("${mq.domain-command.routing-key}")
    private String domainCommandRoutingKey;

    @Bean
    public TopicExchange domainCommandExchange() {
        return new TopicExchange(domainCommandExchange);
    }

    @Bean
    public Queue domainCommandQueue() {
        return QueueBuilder.durable(domainCommandQueue).build();
    }

    @Bean
    public Binding domainCommandBinding() {
        return BindingBuilder
                .bind(domainCommandQueue())
                .to(domainCommandExchange())
                .with(domainCommandRoutingKey);
    }
}
