package edu.seu.rabbitmqClientDemo.demo01;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

/*
    Consumer: receive message from Message Queue
 */
public class Consumer {

    public static final String QUEUE_NAME = "Hello,RabbitMQ";

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("Woshij8dan!");
        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            /*
                消费一个消息
                params:
                1. 消费哪个队列
                2. 消费成功之后是否要自动应答
                3. 消费未成功的回调
                4. 消费者取消消费的回调
             */
            channel.basicConsume(QUEUE_NAME, true,
                    (consumerTag, message) -> System.out.println(Arrays.toString(message.getBody())),
                    consumerTag -> System.out.println("消息消费被中断"));
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
