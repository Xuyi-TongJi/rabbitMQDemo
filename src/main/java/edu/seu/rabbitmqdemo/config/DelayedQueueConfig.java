package edu.seu.rabbitmqdemo.config;

import lombok.Getter;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 延迟队列配置类
 */
@Configuration
public class DelayedQueueConfig {
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    public static final String DELAYED_ROUTING_KEY = "delayed.routingKey";

    @Bean("DELAYED_EXCHANGE")
    public CustomExchange delayedExchange() {
        Map<String, Object> arguments = new HashMap<>();
        // 设置延迟类型
        arguments.put("x-delayed-type", "direct");
        // 设置参数
        // 参数3 是否需要持久化 参数4： 是否需要自动删除
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message",
                true, false, arguments);
    }

    @Bean("DELAYED_QUEUE")
    public Queue delayedQueue() {
        // 不需要绑定任何参数
        return new Queue(DELAYED_QUEUE_NAME);
    }

    @Bean
    public Binding delayedQueueBindingDelayedExchange(@Qualifier("DELAYED_EXCHANGE") Exchange exchange,
                                                      @Qualifier("DELAYED_QUEUE") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}