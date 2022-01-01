package edu.seu.rabbitmqdemo.exchange.receive.impl;

import edu.seu.rabbitmqdemo.exchange.receive.AbstractReceiveLog;

import java.io.IOException;
import java.util.List;

public abstract class AbstractBindingKeyReceiveLog extends AbstractReceiveLog {
    protected final String QUEUE_NAME;
    /*
        一个DirectReceiveLog能绑定一个队列，但这个队列可以绑定多个BINDING_KEY,
        同时，一个BINDING_KEY可以绑定多个队列
    */
    protected final List<String> BINDING_KEYS;

    public AbstractBindingKeyReceiveLog(String EXCHANGE_NAME, String RECEIVE_LOG_NAME,
                                        String QUEUE_NAME, List<String> BINDING_KEYS) {
        super(EXCHANGE_NAME, RECEIVE_LOG_NAME);
        this.QUEUE_NAME = QUEUE_NAME;
        this.BINDING_KEYS = BINDING_KEYS;
    }

    @Override
    protected String getQueue() {
        try {
            // 根据队列名称声明队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            // 绑定BINDING_KEY
            for (String bindingKey: BINDING_KEYS) {
                try {
                    channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, bindingKey);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return QUEUE_NAME;
    }
}