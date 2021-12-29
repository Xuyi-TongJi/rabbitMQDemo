package edu.seu.rabbitmqdemo.demo02;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import edu.seu.rabbitmqdemo.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.Arrays;

/**
 *  工作线程 Consumer
 */
public class Worker implements Runnable {
    private static final String QUEUE_NAME = "hello";
    private final Channel channel = RabbitMQUtils.getChannel();

    @Override
    public void run() {
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(consumerTag + "接收到的消息" + Arrays.toString(message.getBody()));
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "消息被消费者取消消费接口回调逻辑");
        };
        try {
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}