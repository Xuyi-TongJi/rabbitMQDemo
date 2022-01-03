package edu.seu.rabbitmqdemo.config;

import lombok.Getter;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class NullTTLQueueConfig {
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    public static final String QUEUE_C = "QC";
    public static final String BINDING_KEY_DEAD = "YD";
    public static final String BINDING_KEY_C = "XC";

    @Bean("QUEUE_C")
    public Queue queueC() {
        Map<String, Object> args = new HashMap<>(2);
        args.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key", BINDING_KEY_DEAD);
        return QueueBuilder.durable(QUEUE_C).withArguments(args).build();
    }

    @Bean
    public Binding queueCBindingX(@Qualifier("QUEUE_C") Queue queueC,
                                  @Qualifier("X_EXCHANGE") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC).to(xExchange).with(BINDING_KEY_C);
    }
}