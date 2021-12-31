package edu.seu.rabbitmqdemo.exchange.emit.messageInputStrategy;

import edu.seu.rabbitmqdemo.utils.ScannerUtils;

import java.util.Scanner;

public class MessageInputByScanner implements MessageInputStrategy{

    @Override
    public String getMessage() {
        System.out.println("请输入消息");
        return ScannerUtils.getString();
    }
}