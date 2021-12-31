package edu.seu.rabbitmqdemo.exchange.emit.impl;

import edu.seu.rabbitmqdemo.exchange.emit.AbstractEmitLog;
import edu.seu.rabbitmqdemo.exchange.emit.messageBasicPublishStrategy.BindKeyBasicPublish;
import edu.seu.rabbitmqdemo.exchange.emit.messageInputStrategy.MessageInputByScanner;

public class FanoutEmitLog extends AbstractEmitLog {

    public FanoutEmitLog(String EMIT_LOG_NAME, String EXCHANGE_NAME) {
        super(EMIT_LOG_NAME, EXCHANGE_NAME, new MessageInputByScanner(),
                new BindKeyBasicPublish(), null);
    }
}