package com.pickcar.drivehistory.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DriveHistoryMessagingConfig {

    @Value("${mq.drive-history.queue}")
    private String driveHistoryQueue;

    @Value("${mq.drive-history.exchange}")
    private String driveHistoryExchange;

    @Value("${mq.drive-history.routing-key}")
    private String driveHistoryRoutingKey;

    @Bean
    public TopicExchange driveHistoryExchange() {
        return new TopicExchange(driveHistoryExchange);
    }

    @Bean
    public Queue driveHistoryQueue() {
        return QueueBuilder.durable(driveHistoryQueue).build();
    }

    @Bean
    public Binding driveHistoryBinding() {
        return BindingBuilder
                .bind(driveHistoryQueue())
                .to(driveHistoryExchange())
                .with(driveHistoryRoutingKey);
    }
}
