package edu.seu.rabbitmqdemo.listener;

import edu.seu.rabbitmqdemo.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 报警消费者，配合具有备份交换机的高级确认交换机使用
 */
@Slf4j
@Component
public class WarningConsumer {

    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveWarningMessage(Message message) {
        log.error("发现不可路由的消息：{}", message.getBody());
    }
}