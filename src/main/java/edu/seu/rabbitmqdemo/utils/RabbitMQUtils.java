package edu.seu.rabbitmqdemo.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQUtils {

    // ConnectionFactory and Connection Singleton
    private static volatile ConnectionFactory connectionFactory;
    private static volatile Connection connection;

    private RabbitMQUtils() {
    }

    public static Channel getChannel() {
        Channel channel = null;
        if (connectionFactory == null) {
            synchronized (RabbitMQUtils.class) {
                if (connectionFactory == null) {
                    connectionFactory = new ConnectionFactory();
                    connectionFactory.setHost("127.0.0.1");
                    connectionFactory.setUsername("admin");
                    connectionFactory.setPassword("Woshij8dan!");
                }
            }
        }
        if (connection == null) {
            synchronized (RabbitMQUtils.class) {
                if (connection == null) {
                    try {
                        connection = connectionFactory.newConnection();
                    } catch (IOException | TimeoutException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        try {
            channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return channel;
    }
}
