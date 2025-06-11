package com.pickcar.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventRabbitMQConfig {

    public static final String EVENT_QUEUE = "event-queue";
    public static final String EVENT_EXCHANGE = "exchange-event";
    public static final String EVENT_ROUTING_KEY = "event.key";

    @Bean
    public Queue eventQueue() {
        return new Queue(EVENT_QUEUE, true);
    }

    @Bean
    public DirectExchange eventExchange() {
        return new DirectExchange(EVENT_EXCHANGE);
    }

    @Bean
    public Binding eventBinding(Queue eventQueue, DirectExchange eventExchange) {
        return BindingBuilder.bind(eventQueue)
                .to(eventExchange)
                .with(EVENT_ROUTING_KEY);
    }
}
