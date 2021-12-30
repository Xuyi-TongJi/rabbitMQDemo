package edu.seu.rabbitmqdemo.fanOut;

import com.rabbitmq.client.Channel;
import edu.seu.rabbitmqdemo.utils.RabbitMQUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 发消息线程
 */
public class EmitLog implements Runnable{

    private final String EmitLogName;
    private final String EXCHANGE_NAME;
    private final Channel channel = RabbitMQUtils.getProducerChannel();

    public EmitLog(String emitLogName, String EXCHANGE_NAME) {
        EmitLogName = emitLogName;
        this.EXCHANGE_NAME = EXCHANGE_NAME;
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("请输入要发送的消息");
                String message = scanner.nextLine();
                channel.basicPublish(EXCHANGE_NAME, "",
                        null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println(EmitLogName + "成功发送消息" + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
