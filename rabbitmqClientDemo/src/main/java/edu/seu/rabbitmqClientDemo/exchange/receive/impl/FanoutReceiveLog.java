package edu.seu.rabbitmqClientDemo.exchange.receive.impl;

import com.rabbitmq.client.BuiltinExchangeType;
import edu.seu.rabbitmqClientDemo.exchange.receive.AbstractReceiveLog;

import java.io.IOException;

public class FanoutReceiveLog extends AbstractReceiveLog {

    public FanoutReceiveLog(String RECEIVE_LOG_NAME, String EXCHANGE_NAME) {
        super(EXCHANGE_NAME, RECEIVE_LOG_NAME);
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

    @Override
    public void exchangeDeclare() {
        try {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}