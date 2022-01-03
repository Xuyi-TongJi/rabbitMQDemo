package edu.seu.rabbitmqClientDemo.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQUtils {

    // ConnectionFactory and Connection Singleton
    private static volatile ConnectionFactory connectionFactory;
    private static volatile Connection consumerConnection; // 多个消费者共用一个连接
    private static volatile Connection producerConnection;

    private RabbitMQUtils() {

    }

    private static void testConnectionFactoryNull() {
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
    }

    private static void testProducerFactoryNull() {
        if (producerConnection == null) {
            synchronized (RabbitMQUtils.class) {
                if (producerConnection == null) {
                    try {
                        producerConnection = connectionFactory.newConnection();
                    } catch (IOException | TimeoutException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void testConsumerConnectionNull() {
        if (consumerConnection == null) {
            synchronized (RabbitMQUtils.class) {
                if (consumerConnection == null) {
                    try {
                        consumerConnection = connectionFactory.newConnection();
                    } catch (IOException | TimeoutException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static Channel getConsumerChannel() {
        Channel channel = null;
        testConnectionFactoryNull();
        testConsumerConnectionNull();
        try {
            channel = consumerConnection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return channel;
    }

    public static Channel getProducerChannel() {
        Channel channel = null;
        testConnectionFactoryNull();
        testProducerFactoryNull();
        try {
            channel = producerConnection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return channel;
    }
}