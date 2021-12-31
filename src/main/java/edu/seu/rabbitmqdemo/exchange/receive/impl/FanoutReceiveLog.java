package edu.seu.rabbitmqdemo.exchange.receive.impl;

import com.rabbitmq.client.BuiltinExchangeType;
import edu.seu.rabbitmqdemo.exchange.receive.AbstractReceiveLog;

import java.io.IOException;

public class FanoutReceiveLog extends AbstractReceiveLog {

    public FanoutReceiveLog(String RECEIVE_LOG_NAME, String EXCHANGE_NAME) {
        super(EXCHANGE_NAME, RECEIVE_LOG_NAME);
    }

    @Override
    protected void exchangeDeclare() {
        try {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getQueue() {
        try {
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, "");
            return queueName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}