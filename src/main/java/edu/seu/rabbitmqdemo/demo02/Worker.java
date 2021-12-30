package edu.seu.rabbitmqdemo.demo02;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import edu.seu.rabbitmqdemo.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 *  工作线程 Consumer
 */
public class Worker implements Runnable {
    private static final String QUEUE_NAME = "hello";
    private final Channel channel = RabbitMQUtils.getConsumerChannel();
    private final long sleepTime;
    private final int prefetchCount; // 预取值, 信道中允许堆积的消息数

    public Worker(long sleepTime, int prefetchCount) {
        this.sleepTime = sleepTime;
        this.prefetchCount = prefetchCount;
    }

    @Override
    public void run() {
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            // 模拟处理消息的时间
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String messageBody = new String(message.getBody(), UTF_8);
            System.out.println(consumerTag + "接收到的消息" + Arrays.toString(message.getBody()));
            // 设置不公平分发
            channel.basicQos(prefetchCount);
            // 设置手动应答 参数2为是否批量应答
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };
        CancelCallback cancelCallback = (consumerTag) -> System.out.println(consumerTag + "消息被消费者取消消费接口回调逻辑");
        try {
            boolean autoACK = false;
            // 处理消息，手动应答
            channel.basicConsume(QUEUE_NAME, autoACK, deliverCallback, cancelCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}