package edu.seu.rabbitmqClientDemo.exchange.emit.messageBasicPublishStrategy;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BindKeyBasicPublish implements MessageBasicPublishStrategy{
    @Override
    public void publishMessage(Channel channel, String EXCHANGE_NAME, String message, String BINDING_KEY) {
        try {
            channel.basicPublish(EXCHANGE_NAME, BINDING_KEY,
                    null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
