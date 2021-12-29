package edu.seu.rabbitmqdemo.concurrent;

import java.util.Scanner;

/**
 * 可用于发送字符串消息的消息队列
 * 组合一个messageQueue
 */
public class Producer implements Runnable{

    private final SimpleMessageQueue<String> messageQueue;

    public Producer(SimpleMessageQueue<String> messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("请输入消息");
            Message<String> message = getMessage();
            if ("stop".equals(message.getData())) {
                System.out.println("任务线程结束");
                break;
            }
            messageQueue.put(message);
        }
    }

    private Message<String> getMessage() {
        Scanner scanner = new Scanner(System.in);
        String messageString = scanner.nextLine();
        return new Message<>(messageString);
    }
}