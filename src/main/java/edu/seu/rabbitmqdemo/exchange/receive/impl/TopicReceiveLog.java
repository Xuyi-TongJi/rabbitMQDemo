package edu.seu.rabbitmqdemo.exchange.receive.impl;

import com.rabbitmq.client.BuiltinExchangeType;

import java.io.IOException;
import java.util.List;

public class TopicReceiveLog extends AbstractBindingKeyReceiveLog {

    public TopicReceiveLog(String EXCHANGE_NAME, String RECEIVE_LOG_NAME, String QUEUE_NAME, List<String> BINDING_KEYS) {
        super(EXCHANGE_NAME, RECEIVE_LOG_NAME, QUEUE_NAME, BINDING_KEYS);
    }

    @Override
    public void exchangeDeclare() {
        try {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
