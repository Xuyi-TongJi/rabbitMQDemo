package edu.seu.rabbitmqdemo.fanOut;

public class Client {
    public static void main(String[] args) {
        String EXCHANGE_NAME = "EXCHANGE_NAME";
        // 接收方声明了交换机，先定义
        new Thread(new ReceiveLog("RECEIVE1", EXCHANGE_NAME)).start();
        new Thread(new ReceiveLog("RECEIVE2", EXCHANGE_NAME)).start();
        new Thread(new EmitLog("EMIT", EXCHANGE_NAME)).start();
    }
}
