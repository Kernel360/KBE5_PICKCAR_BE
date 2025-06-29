package com.pickcar.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CycleMessagingConfig {

    @Value("${mq.cycle.queue}")
    private String queueName;

    @Value("${mq.cycle.exchange}")
    private String exchange;

    @Value("${mq.cycle.routing-key}")
    private String routingKey;

    @Bean
    public Queue cycleQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public TopicExchange cycleExchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding cycleBinding(Queue cycleQueue, TopicExchange cycleExchange) {
        return BindingBuilder.bind(cycleQueue)
                .to(cycleExchange)
                .with(routingKey+".*");
    }
}