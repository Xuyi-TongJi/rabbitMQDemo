package edu.seu.rabbitmqdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class MyCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    private final RabbitTemplate rabbitTemplate;

    public MyCallback(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 在交换机收到或未收到消息时，给予回复
     * @param correlationData 保存回调消息的ID及相关信息
     * @param ack 交换机收到消息返回true，失败为false
     * @param cause 引起失败的原因，发送成功为null
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : null;
        if (ack) {
            log.info("交换机已经收到了消息,id为{}", id);
        } else {
            log.info("交换机未收到id为{}的消息,原因为{}", id, cause);
        }
    }

    /**
     * 在当消息传递过程中不可达目的地时将消息返回给生产者
     * @param returnedMessage 回退消息
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error("消息{}, 被交换机{}退回，退回原因:{}，路由key:{}",
                new String(returnedMessage.getMessage().getBody()),
                returnedMessage.getExchange(),
                returnedMessage.getReplyText(),
                returnedMessage.getRoutingKey()
                );
    }

    // 注入
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }
}