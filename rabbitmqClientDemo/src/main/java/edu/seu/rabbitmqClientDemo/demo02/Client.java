package edu.seu.rabbitmqClientDemo.demo02;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Client {
    public static void main(String[] args) {
        // 启动三个工作线程
        Thread workerThread01 = new Thread(new Worker(1, 2), "workerThread-1");
        Thread workerThread02 = new Thread(new Worker(1,2), "workerThread-2");
        Thread workerThread03 = new Thread(new Worker(50000, 5), "workerThread-3");
        workerThread01.start();
        workerThread02.start();
        workerThread03.start();
        Thread taskThread01 = new Thread(new Task(), "taskThread");
        taskThread01.start();
    }
}