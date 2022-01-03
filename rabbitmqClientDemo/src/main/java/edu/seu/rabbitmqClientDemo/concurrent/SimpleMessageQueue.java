package edu.seu.rabbitmqClientDemo.concurrent;

import java.util.LinkedList;

public class SimpleMessageQueue<T> {

    private final LinkedList<Message<T>> list = new LinkedList<>();
    private final int capacity;
    private final String queueName;
/*    private final LinkedList<Producer> producerList = new LinkedList<>();
    private final LinkedList<Consumer> consumerList = new LinkedList<>();*/

    public SimpleMessageQueue(int capacity, String queueName) {
        this.capacity = capacity;
        this.queueName = queueName;
    }

    /**
     * 生产者将消息存入消息队列
     * @param message 需要存放的消息
     */
    public void put(Message<T> message) {
        synchronized (list) {
            while (list.size() == capacity) {
                try {
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // produce
            list.addLast(message);
            list.notifyAll();
        }
    }

    /**
     * 消息队列取出消息
     * @return message
     */
    public Message<T> get() {
        synchronized (list) {
            while (list.size() == 0) {
                try {
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // consume
            Message<T> message = list.removeLast();
            list.notifyAll();
            return message;
        }
    }

    public void listStatus() {
        System.out.println(queueName + "-----status");
        for (Message<T> message:
             list) {
            System.out.println(message);
        }
    }
}
