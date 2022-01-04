package edu.seu.rabbitmqClientDemo.priortyQueue;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        new Thread(new Producer("HELLO_QUEUE")).start();
        Thread.sleep(10000);
        new Thread(new Consumer("HELLO_QUEUE")).start();
    }
}