package edu.seu.rabbitmqdemo.config;

import lombok.Getter;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置类：声明交换机，队列。并将其绑定。
 */
@Configuration
public class TTLQueueConfig {
    public static final String X_EXCHANGE = "X";
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String QUEUE_DEAD = "QD"; // 死信队列，即延迟队列
    public static final String BINDING_KEY_A = "XA";
    public static final String BINDING_KEY_B = "XB";
    public static final String BINDING_KEY_DEAD = "YD";

    @Bean("X_EXCHANGE")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    @Bean("Y_DEAD_LETTER_EXCHANGE")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    @Bean("QUEUE_A")
    public Queue queueA() {
        return getTTLQueue(10000L, QUEUE_A);
    }

    @Bean("QUEUE_B")
    public Queue queueB() {
        return getTTLQueue(40000L, QUEUE_B);
    }

    @Bean("QUEUE_DEAD")
    public Queue queueDead() {
        return QueueBuilder.durable(QUEUE_DEAD).build();
    }

    /**
     * 绑定普通队列和普通交换机
     * @param queueA 队列
     * @param xExchange 交换机
     * @return Binding
     */
    @Bean
    public Binding queueABindingX(@Qualifier("QUEUE_A") Queue queueA,
                                  @Qualifier("X_EXCHANGE") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with(BINDING_KEY_A);
    }

    @Bean
    public Binding queueBBindingX(@Qualifier("QUEUE_B") Queue queueB,
                                  @Qualifier("X_EXCHANGE") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with(BINDING_KEY_B);
    }

    @Bean
    public Binding queueDBindingY(@Qualifier("QUEUE_DEAD") Queue queueDEAD,
                                  @Qualifier("Y_DEAD_LETTER_EXCHANGE") DirectExchange yExchange) {
        return BindingBuilder.bind(queueDEAD).to(yExchange).with(BINDING_KEY_DEAD);
    }

    /**
     * 设置普通队列参数
     * @param TTL 消息延迟时间
     * @return 参数Map
     */
    private Queue getTTLQueue(long TTL, String QueueName) {
        Map<String, Object> arguments = new HashMap<>(3);
        // TTL 10s
        arguments.put("x-message-ttl", TTL);
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", BINDING_KEY_DEAD);
        return QueueBuilder.durable(QueueName).withArguments(arguments).build();
    }
}