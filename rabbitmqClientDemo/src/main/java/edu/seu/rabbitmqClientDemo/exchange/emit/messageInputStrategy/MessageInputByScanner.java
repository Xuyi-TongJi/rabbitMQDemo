package edu.seu.rabbitmqClientDemo.exchange.emit.messageInputStrategy;

import edu.seu.rabbitmqClientDemo.utils.ScannerUtils;

public class MessageInputByScanner implements MessageInputStrategy{

    @Override
    public String getMessage() {
        System.out.println("请输入消息");
        return ScannerUtils.getString();
    }
}