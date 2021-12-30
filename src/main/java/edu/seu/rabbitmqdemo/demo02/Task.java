package edu.seu.rabbitmqdemo.demo02;

import com.rabbitmq.client.Channel;
import edu.seu.rabbitmqdemo.utils.RabbitMQUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 生产者：用于发送大量消息 Producer
 */
public class Task implements Runnable{

    private static final String QUEUE_NAME = "hello";
    private final Channel channel = RabbitMQUtils.getProducerChannel();

    // 从控制台中接收信息
    private String getMessageString() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入信息：");
        return scanner.nextLine();
    }

    @Override
    public void run() {
        try {
            // declare a message
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            // publish a message
            while (true) {
                String message = getMessageString();
                if ("stop".equals(message)) {
                    break;
                }
                channel.basicPublish("", QUEUE_NAME, null,
                        message.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
