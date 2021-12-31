package edu.seu.rabbitmqdemo.exchange.receive.impl;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DirectReceiveLog extends AbstractBindingKeyReceiveLog {

    public DirectReceiveLog(String EXCHANGE_NAME, String RECEIVE_LOG_NAME,
                            String QUEUE_NAME, List<String> BINDING_KEYS) {
        super(EXCHANGE_NAME, RECEIVE_LOG_NAME, QUEUE_NAME, BINDING_KEYS);
    }

    @Override
    protected void exchangeDeclare() {
        try {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected DeliverCallback deliverCallbackMethod() {
        return (consumeTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("消息队列" + QUEUE_NAME + ", 绑定key："
                    + delivery.getEnvelope().getRoutingKey() + "，消息：" + message);
        };
    }
}
