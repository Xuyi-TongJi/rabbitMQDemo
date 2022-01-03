package edu.seu.rabbitmqClientDemo.deadQueue;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import edu.seu.rabbitmqClientDemo.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 支持死信队列的消费者
 * 普通队列需要将死信转发到死信交换机和死信队列
 */
public class Consumer implements Runnable {

    private final Channel channel = RabbitMQUtils.getConsumerChannel();
    private final String NORMAL_EXCHANGE;
    private final String DEAD_EXCHANGE;
    private final String NORMAL_QUEUE;
    private final String DEAD_QUEUE;
    private final String NORMAL_BINDING_KEY;
    private final String DEAD_BINDING_KEY;
    private final String consumerName;
    private volatile boolean stop;

    public Consumer(String NORMAL_EXCHANGE, String DEAD_EXCHANGE, String NORMAL_QUEUE,
                    String DEAD_QUEUE, String NORMAL_BINDING_KEY, String DEAD_BINDING_KEY, String consumerName) {
        this.NORMAL_EXCHANGE = NORMAL_EXCHANGE;
        this.DEAD_EXCHANGE = DEAD_EXCHANGE;
        this.NORMAL_QUEUE = NORMAL_QUEUE;
        this.DEAD_QUEUE = DEAD_QUEUE;
        this.NORMAL_BINDING_KEY = NORMAL_BINDING_KEY;
        this.DEAD_BINDING_KEY = DEAD_BINDING_KEY;
        this.consumerName = consumerName;
    }

    @Override
    public void run() {
        // 声明交换机
        try {
            channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
            channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
            // 声明队列
            // 普通队列声明参数
            Map<String, Object> arguments = new HashMap<>();
            arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE); // 转发到死信交换机
            arguments.put("x-dead-letter-routing-key", DEAD_BINDING_KEY); // 死信交换机路由到死信队列
            channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
            channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
            channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, NORMAL_BINDING_KEY);
            channel.queueBind(DEAD_EXCHANGE, DEAD_EXCHANGE, DEAD_BINDING_KEY);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DeliverCallback deliverCallback = (consumeTag, delivery) -> {
            String msg = new String(delivery.getBody());
            System.out.println(consumerName + "接收到：" + msg);
        };
        CancelCallback cancelCallback = consumeTag -> {
        };
        try {
            channel.basicConsume(NORMAL_QUEUE, true, deliverCallback, cancelCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}