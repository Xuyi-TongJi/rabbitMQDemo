package edu.seu.rabbitmqSpringboot.controller;

import edu.seu.rabbitmqSpringboot.config.DelayedQueueConfig;
import edu.seu.rabbitmqSpringboot.config.NullTTLQueueConfig;
import edu.seu.rabbitmqSpringboot.config.TTLQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/ttl")
public class MessageController {

    private final RabbitTemplate rabbitTemplate;

    public MessageController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 基于死信队列实现的延迟队列
     * 缺点是会产生较多的队列冗余
     * @param message 消息
     */
    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message) {
        log.info("当前时间：{}, 发送一条信息给两个TTL队列:{}", new Date(), message);
        // 参数为name，非beanName
        rabbitTemplate.convertAndSend(TTLQueueConfig.X_EXCHANGE, TTLQueueConfig.BINDING_KEY_A,
                "消息来自于TTL为10s的队列" + message);
        rabbitTemplate.convertAndSend(TTLQueueConfig.X_EXCHANGE, TTLQueueConfig.BINDING_KEY_B,
                "消息来自于TTL为40s的队列" + message);
    }

    /**
     * 接收客户端的具有过期时间属性的消息，可以发送至未设置TTL的队列，以优化延迟队列代码
     * 实际上，该方法不可取
     * @param message 消息内容
     * @param ttlTime 过期时间 String 毫秒值
     */
    @Deprecated
    @GetMapping("/sendExpiration/{message}/{TTLTime}")
    public void sendMessage(@PathVariable("message") String message,
                            @PathVariable("TTLTime") String ttlTime) {
        log.info("当前时间:{}, 发送一条时长{}毫秒的消息{}至队列",
                new Date(), ttlTime, message);
        rabbitTemplate.convertAndSend(
                NullTTLQueueConfig.Y_DEAD_LETTER_EXCHANGE,
                NullTTLQueueConfig.BINDING_KEY_C,
                "消息来自于TTL为" + ttlTime + "的队列" + message,
                msg -> {
                    // 设置延迟时间
                    msg.getMessageProperties().setExpiration(ttlTime);
                    return msg;
                });
    }

    /**
     * 基于插件实现的消息队列发送逻辑
     * 推荐使用
     * @param message 消息
     * @param delayedTime 延迟时间 Integer 毫秒值
     */
    @GetMapping("/sendDelayed/{message}/{delayedTime}")
    public void sendMessage(@PathVariable("message") String message,
                            @PathVariable("delayedTime") Integer delayedTime) {
        log.info("当前时间:{}, 发送一条时长{}毫秒的消息{}至队列",
                new Date(), delayedTime, message);
        rabbitTemplate.convertAndSend(
                DelayedQueueConfig.DELAYED_EXCHANGE_NAME,
                DelayedQueueConfig.DELAYED_ROUTING_KEY,
                message,
                msg -> {
                    // 设置延迟时间
                    msg.getMessageProperties().setDelay(delayedTime);
                    return msg;
                });
    }
}