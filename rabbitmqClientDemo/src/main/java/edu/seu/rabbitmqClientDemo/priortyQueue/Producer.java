package edu.seu.rabbitmqClientDemo.priortyQueue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import edu.seu.rabbitmqClientDemo.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Producer implements Runnable {
    private final Channel channel = RabbitMQUtils.getProducerChannel();
    private final String QUEUE_NAME;

    public Producer(String QUEUE_NAME) {
        this.QUEUE_NAME = QUEUE_NAME;
    }

    @Override
    public void run() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-max-priority", 10);
        try {
            channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);
            String message;
            for (int i = 0; i < 10; i++) {
                message = "info" + i;
                if (i == 5) {
                    AMQP.BasicProperties properties =
                            new AMQP.BasicProperties().builder().priority(5).build();
                    channel.basicPublish("", QUEUE_NAME, properties, message.getBytes());
                } else {
                    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
