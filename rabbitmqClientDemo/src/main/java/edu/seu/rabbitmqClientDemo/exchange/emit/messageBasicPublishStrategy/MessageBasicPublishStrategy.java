package edu.seu.rabbitmqClientDemo.exchange.emit.messageBasicPublishStrategy;

import com.rabbitmq.client.Channel;

public interface MessageBasicPublishStrategy {

    default void publishMessage(Channel channel, String EXCHANGE_NAME, String message) {
        publishMessage(channel, EXCHANGE_NAME, message, "");
    }

    void publishMessage(Channel channel, String EXCHANGE_NAME, String message, String BINDING_KEY);
}