package edu.seu.rabbitmqdemo.fanOut;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import edu.seu.rabbitmqdemo.utils.RabbitMQUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 接收消息线程
 */
public class ReceiveLog implements Runnable{

    private final String ReceiveLogName;
    private final Channel channel = RabbitMQUtils.getConsumerChannel();
    private final String EXCHANGE_NAME;

    public ReceiveLog(String receiveLogName, String exchange_name) {
        ReceiveLogName = receiveLogName;
        this.EXCHANGE_NAME = exchange_name;
    }

    @Override
    public void run() {
        try {
            // 声明交换机
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            // 声明一个临时队列
            String queueName = channel.queueDeclare().getQueue();
            // 绑定交换机与队列
            channel.queueBind(queueName, EXCHANGE_NAME, "");
            // 进行消息的接收
            DeliverCallback deliverCallback = (consumeTag, message) -> {
                System.out.println(ReceiveLogName + "控制台打印接收到的消息" + new String(message.getBody(),
                        StandardCharsets.UTF_8));
            };
            channel.basicConsume(queueName, true, deliverCallback, consumeTag -> {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}