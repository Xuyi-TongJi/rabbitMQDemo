package edu.seu.rabbitmqdemo.deadQueue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import edu.seu.rabbitmqdemo.utils.RabbitMQUtils;
import edu.seu.rabbitmqdemo.utils.ScannerUtils;

import java.io.IOException;

// 生产者
// 二阶段终止
public class Producer implements Runnable {

    private final String EXCHANGE_NAME;
    private final String producerName;
    private volatile boolean stop = false;
    private final Channel channel = RabbitMQUtils.getProducerChannel();

    public Producer(String EXCHANGE_NAME, String producerName) {
        this.EXCHANGE_NAME = EXCHANGE_NAME;
        this.producerName = producerName;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(producerName + ",请输入消息");
            String message = ScannerUtils.getString();
            System.out.println("请输入BINDING_KEY");
            String bindingKey = ScannerUtils.getString();
            if ("stop".equals(message)) {
                stop();
            }
            // 设置TTL时间
            try {
                AMQP.BasicProperties build
                        = new AMQP.BasicProperties().builder().expiration("10000").build();
                channel.basicPublish(EXCHANGE_NAME, bindingKey, build, message.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (stop) {
                System.out.println("线程安全终止");
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
    }

    /**
     * 终止线程逻辑
     */
    private void stop() {
        stop = true;
        Thread.currentThread().interrupt();
    }
}