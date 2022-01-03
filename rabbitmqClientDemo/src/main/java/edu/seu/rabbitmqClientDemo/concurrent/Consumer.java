package edu.seu.rabbitmqClientDemo.concurrent;

/**
 * 消费者线程，可以消费字符串类型数据
 * 组合一个Consumer
 */
public class Consumer implements Runnable {

    private final SimpleMessageQueue<String> messageQueue;

    public Consumer(SimpleMessageQueue<String> messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        Message<String> message;
        while (true) {
            message = this.get();
            deliverCallback(message);
        }
    }

    private void deliverCallback(Message<String> message) {
        System.out.println(Thread.currentThread().getName() + ":" +  message.getData());
    }

    private Message<String> get() {
        return this.messageQueue.get();
    }
}
