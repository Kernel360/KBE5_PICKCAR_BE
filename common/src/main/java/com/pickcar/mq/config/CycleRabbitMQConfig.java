package com.pickcar.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CycleRabbitMQConfig {

    public static final String CYCLE_QUEUE = "cycle-queue";
    public static final String CYCLE_EXCHANGE = "exchange-cycle";
    public static final String CYCLE_ROUTING_KEY = "cycle.key";

    @Bean
    public Queue cycleQueue() {
        return new Queue(CYCLE_QUEUE, true);
    }

    @Bean
    public DirectExchange cycleExchange() {
        return new DirectExchange(CYCLE_EXCHANGE);
    }

    @Bean
    public Binding cycleBinding(Queue cycleQueue, DirectExchange cycleExchange) {
        return BindingBuilder.bind(cycleQueue)
                .to(cycleExchange)
                .with(CYCLE_ROUTING_KEY);
    }
}