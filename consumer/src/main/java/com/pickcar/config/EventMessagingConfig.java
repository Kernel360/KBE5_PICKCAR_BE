package com.pickcar.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventMessagingConfig {

    @Value("${mq.event.queue}")
    private String queueName;

    @Value("${mq.event.exchange}")
    private String exchange;

    @Value("${mq.event.routing-key}")
    private String routingKey;

    @Bean
    public Queue eventQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public DirectExchange eventExchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding eventBinding(Queue eventQueue, DirectExchange eventExchange) {
        return BindingBuilder.bind(eventQueue)
                .to(eventExchange)
                .with(routingKey);
    }
}
