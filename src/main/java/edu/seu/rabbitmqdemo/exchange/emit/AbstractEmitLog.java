package edu.seu.rabbitmqdemo.exchange.emit;

import com.rabbitmq.client.Channel;
import edu.seu.rabbitmqdemo.exchange.emit.bindingKeyInputStrategy.BindingKeyInputStrategy;
import edu.seu.rabbitmqdemo.exchange.emit.messageBasicPublishStrategy.MessageBasicPublishStrategy;
import edu.seu.rabbitmqdemo.exchange.emit.messageInputStrategy.MessageInputStrategy;
import edu.seu.rabbitmqdemo.utils.RabbitMQUtils;

/**
 * 抽象的发布者类，策略模式
 */
public abstract class AbstractEmitLog implements Runnable {

    protected final String EMIT_LOG_NAME;
    protected final String EXCHANGE_NAME;
    protected final Channel channel = RabbitMQUtils.getProducerChannel();
    protected final MessageInputStrategy messageInputStrategy;
    protected final MessageBasicPublishStrategy messageBasicPublishStrategy;
    protected final BindingKeyInputStrategy bindingKeyInputStrategy;

    public AbstractEmitLog(String EMIT_LOG_NAME, String EXCHANGE_NAME,
                           MessageInputStrategy messageInputStrategy,
                           MessageBasicPublishStrategy messageBasicPublishStrategy,
                           BindingKeyInputStrategy bindingKeyInputStrategy) {
        this.EMIT_LOG_NAME = EMIT_LOG_NAME;
        this.EXCHANGE_NAME = EXCHANGE_NAME;
        this.messageInputStrategy = messageInputStrategy;
        this.messageBasicPublishStrategy = messageBasicPublishStrategy;
        this.bindingKeyInputStrategy = bindingKeyInputStrategy;
    }

    @Override
    public final void run() {
        while (true) {
            String message = messageInput();
            messageBasicPublish(message);
        }
    }

    protected void messageBasicPublish(String message) {
        if (bindingKeyInputStrategy != null) {
            // 需要bindingKey
            String bindingKey = bindingKeyInputStrategy.getBindingKey();
            this.messageBasicPublishStrategy.publishMessage(this.channel, this.EXCHANGE_NAME, message, bindingKey);
        } else {
            // 不需要bindingKey
            this.messageBasicPublishStrategy.publishMessage(this.channel, this.EXCHANGE_NAME, message);
        }
    }

    protected String messageInput() {
        return this.messageInputStrategy.getMessage();
    }
}