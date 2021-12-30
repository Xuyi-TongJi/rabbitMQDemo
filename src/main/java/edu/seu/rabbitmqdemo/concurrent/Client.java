package edu.seu.rabbitmqdemo.concurrent;

public class Client {
    public static void main(String[] args) {
        SimpleMessageQueue<String> messageQueue = new SimpleMessageQueue<>(5, "HELLO_QUEUE");
        new Thread(new Producer(messageQueue), "producer01").start();
        new Thread(new Consumer(messageQueue), "consumer01").start();
        new Thread(new Consumer(messageQueue), "consumer02").start();
    }
}