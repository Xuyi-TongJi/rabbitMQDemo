package edu.seu.rabbitmqSpringboot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 发布确认高级内容的环境配置类
 * 备份交换机仅当交换机接收到消息但队列未接收到（路由错误等）时执行，与ReturnsBack相同
 * 但备份交换机的优先级高于回退RabbitTemplate.ReturnsBack，前者存在并执行后，ReturnsBack将不执行
 */
@Configuration
public class ConfirmConfig {
    // 有确认功能的交换机
    public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";
    public static final String CONFIRM_QUEUE_NAME = "confirm_queue";
    public static final String BINDING_KEY = "key";
    // 备份交换机
    public static final String BACKUP_QUEUE_NAME = "backup_queue";
    public static final String BACKUP_EXCHANGE_NAME = "backup_exchange";
    // 报警队列
    public static final String WARNING_QUEUE_NAME = "warning_queue";

    @Bean("CONFIRM_EXCHANGE")
    public DirectExchange confirmExchange() {
        // 使用构建器，指定备份交换机
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true).withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME).build();
    }

    @Bean("BACKUP_EXCHANGE")
    public FanoutExchange backupExchange() {
        // 扇出（广播）交换机不需要Routing_Key
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    @Bean("CONFIRM_QUEUE")
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    @Bean("BACKUP_QUEUE")
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    @Bean("WARNING_QUEUE")
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    @Bean
    public Binding confirmQueueBindingConfirmExchange(@Qualifier("CONFIRM_QUEUE") Queue queue,
                                                      @Qualifier("CONFIRM_EXCHANGE") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(BINDING_KEY).noargs();
    }

    @Bean
    public Binding backupQueueBindingBackupExchange(@Qualifier("BACKUP_QUEUE") Queue queue,
                                                      @Qualifier("BACKUP_EXCHANGE") FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public Binding warningQueueBindingBackupExchange(@Qualifier("WARNING_QUEUE") Queue queue,
                                                    @Qualifier("BACKUP_EXCHANGE") FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }
}