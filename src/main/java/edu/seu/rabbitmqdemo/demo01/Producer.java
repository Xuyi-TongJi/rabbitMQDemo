package edu.seu.rabbitmqdemo.demo01;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *  Producer: send message to the Message Queue
 */
public class Producer {

    private final static String QUEUE_NAME = "Hello,RabbitMQ";

    public static void main(String[] args) {
        // 创建一个连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("Woshij8dan!");
        try {
            Connection connection = connectionFactory.newConnection();
            // 获取信道
            Channel channel = connection.createChannel();
            // 连接交换机(连接队列表示使用默认交换机)
            /*
                生成一个队列：
                params:
                1. 队列名称
                2. 是否持久化，默认存储在内存中
                3. 该队列是否只供一个消费者进行消费（是否进行消息共享）
                4. 是否自动删除，最后一个消费者断开连接以后，该队列是否自动删除
             */
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            // 发消息
            String message = "HELLO, WORLD";
            /*
                发送一个消息
                params:
                1. 发送到哪个交换机
                2. 表示路由的key值
                3. 其他参数信息
                4. 发送消息的消息体(字节)
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
