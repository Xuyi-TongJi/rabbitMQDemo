package edu.seu.rabbitmqdemo.confirmMessage;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import edu.seu.rabbitmqdemo.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *  单个确认模式
 */
public class ConfirmMessageClient {

    private static final Channel channel = RabbitMQUtils.getConsumerChannel();
    // 批量发送消息的个数
    private static final int MESSAGE_COUNT = 1000;

    // Client
    public static void main(String[] args) throws IOException {
        publishMessageAsync();
    }

    private static String setChannel() throws IOException {
        // 队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        return queueName;
    }

    public static void publishMessageIndividually() throws IOException, InterruptedException {
        String queueName = setChannel();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发布成功");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end - begin); // 168ms/ 1000条
    }

    public static void publishMessageBatch() throws IOException, InterruptedException {
        String queueName = setChannel();
        int batchSize = 100;
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            // 每100条消息确认一次发布
            if (i % batchSize == 0) {
                boolean flag = channel.waitForConfirms();
                if (flag) {
                    System.out.println("消息发布成功");
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end - begin); // 35ms/ 1000条
    }

    public static void publishMessageAsync() throws IOException {
        String queueName = setChannel();

        /*
            线程安全有序的一个哈希表，适用于高并发的情况
            轻松地将序号与消息进行关联
            轻松地批量删除条目，只要给到序号
            支持高并发-->选择ConcurrentSkipListMap容器
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms
                = new ConcurrentSkipListMap<>();

        // 确认成功回调
        /*
            参数1：消息标志；参数2：是否批量处理
         */
        ConfirmCallback confirmCallback = (deliveryTag, multiple) -> {
            // 获得已经确认的消息
            // 是否批量
            if (multiple) {
                ConcurrentNavigableMap<Long, String> confirmed
                        = outstandingConfirms.headMap(deliveryTag, true);
                confirmed.clear();
            } else {
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认的消息" + deliveryTag);
        };
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            String message = outstandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息" + message);
        };
        // 准备消息的监听器，监听哪些消息成功了，哪些消息失败了
        /*
            参数1：监听成功的回调函数
            参数2：监听失败的回调函数
         */
        channel.addConfirmListener(confirmCallback, nackCallback); // fork

        // 批量发送消息
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            // 记录下所有要发送的消息
            channel.basicPublish("", queueName, null, message.getBytes());
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
        }
        System.out.println("消息发布成功");
        long end = System.currentTimeMillis();
        System.out.println(end - begin); // 16ms/1000条
    }
}
