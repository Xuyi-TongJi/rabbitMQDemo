package edu.seu.rabbitmqdemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/ttl")
@Slf4j
public class MessageController {

    private final RabbitTemplate rabbitTemplate;

    public MessageController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message) {
        log.info("当前时间：{}, 发送一条信息给两个TTL队列:{}", new Date(), message);
        // 参数为name，非beanName
        rabbitTemplate.convertAndSend("X", "XA", "消息来自于TTL为10s的队列" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自于TTL为40s的队列" + message);
    }

    /**
     * 接收客户端的具有过期时间属性的消息，可以发送至未设置TTL的队列，以优化延迟队列代码
     *
     * @param message 消息内容
     * @param ttlTime 过期时间
     */
    @GetMapping("/sendExpiration/{message}/{TTLTime}")
    public void sendMessage(@PathVariable("message") String message,
                            @PathVariable("TTLTime") String ttlTime) {
        log.info("当前时间:{}, 发送一条时长{}毫秒的消息{}至队列",
                new Date(), ttlTime, message);
        rabbitTemplate.convertAndSend("X", "XC",
                "消息来自于TTL为" + ttlTime + "的队列" + message,
                msg -> {
                    msg.getMessageProperties().setExpiration(ttlTime);
                    return msg;
                });
    }
}