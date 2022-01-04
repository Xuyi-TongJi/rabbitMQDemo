package edu.seu.rabbitmqClientDemo.priortyQueue;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import edu.seu.rabbitmqClientDemo.utils.RabbitMQUtils;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Consumer implements Runnable{
    private final String QUEUE_NAME;
    private final Channel channel = RabbitMQUtils.getConsumerChannel();

    public Consumer(String QUEUE_NAME) {
        this.QUEUE_NAME = QUEUE_NAME;
    }

    @Override
    public void run() {
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String messageBody = new String(message.getBody(), UTF_8);
            System.out.println(consumerTag + "接收到的消息" + messageBody);
        };
        CancelCallback cancelCallback =
                (consumerTag) -> System.out.println(consumerTag + "消息被消费者取消消费接口回调逻辑");
        try {
            boolean autoACK = true;
            // 处理消息，手动应答
            channel.basicConsume(QUEUE_NAME, autoACK, deliverCallback, cancelCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
