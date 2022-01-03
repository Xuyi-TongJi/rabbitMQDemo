package edu.seu.rabbitmqdemo.listener;

import edu.seu.rabbitmqdemo.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NormalConsumer {

    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveConfirmMessage(Message message) {
        log.info("接受到的队列{}消息:{}",
                ConfirmConfig.CONFIRM_QUEUE_NAME,
                new String(message.getBody()));
    }
}