package edu.seu.rabbitmqSpringboot.controller;

import edu.seu.rabbitmqSpringboot.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/confirm")
public class ConfirmedMessageController {

    private final RabbitTemplate rabbitTemplate;

    public ConfirmedMessageController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 确认发布高级测试接口，用以测试正常发送，发送到错误交换机（交换机故障），
     * 发送到正确交换机但错误路由key（队列故障）的情况
     * @param message 接收客户端发送的message
     */
    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

/*        rabbitTemplate.convertAndSend(
                ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.BINDING_KEY,
                message,
                correlationData
        );*/

        // 模拟错误发送BINDING_KEY
        rabbitTemplate.convertAndSend(
                ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.BINDING_KEY + "123",
                message,
                correlationData
        );

        // 模拟发送到错误的EXCHANGE
        rabbitTemplate.convertAndSend(
                ConfirmConfig.CONFIRM_EXCHANGE_NAME + "123",
                ConfirmConfig.BINDING_KEY,
                message,
                correlationData
        );
        log.info("发送消息内容:{}", message);
    }
}