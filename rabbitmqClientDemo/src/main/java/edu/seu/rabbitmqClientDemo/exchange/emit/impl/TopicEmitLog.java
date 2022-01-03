package edu.seu.rabbitmqClientDemo.exchange.emit.impl;

import edu.seu.rabbitmqClientDemo.exchange.emit.AbstractEmitLog;
import edu.seu.rabbitmqClientDemo.exchange.emit.bindingKeyInputStrategy.BindingKeyInputByScanner;
import edu.seu.rabbitmqClientDemo.exchange.emit.messageBasicPublishStrategy.BindKeyBasicPublish;
import edu.seu.rabbitmqClientDemo.exchange.emit.messageInputStrategy.MessageInputByScanner;

public class TopicEmitLog extends AbstractEmitLog {
    public TopicEmitLog(String EMIT_LOG_NAME, String EXCHANGE_NAME) {
        super(EMIT_LOG_NAME, EXCHANGE_NAME, new MessageInputByScanner(),
                new BindKeyBasicPublish(), new BindingKeyInputByScanner());
    }
}