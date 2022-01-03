package edu.seu.rabbitmqClientDemo.exchange.receive;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import edu.seu.rabbitmqClientDemo.utils.RabbitMQUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 抽象的接收方：模版模式
 */
public abstract class AbstractReceiveLog implements Runnable{

    protected final Channel channel = RabbitMQUtils.getConsumerChannel();
    protected final String EXCHANGE_NAME;
    protected final String RECEIVE_LOG_NAME;

    public AbstractReceiveLog(String EXCHANGE_NAME, String RECEIVE_LOG_NAME) {
        this.EXCHANGE_NAME = EXCHANGE_NAME;
        this.RECEIVE_LOG_NAME = RECEIVE_LOG_NAME;
    }

    /**
     * 接收方执行逻辑，实现类不可重写
     */
    @Override
    public final void run() {
        try {
            // 声明交换机
            exchangeDeclare();
            // 声明一个临时队列
            String queueName = this.getQueue();
            // 进行消息的接收
            DeliverCallback deliverCallback = this.deliverCallbackMethod();
            CancelCallback cancelCallback = this.cancelCallbackMethod();
            channel.basicConsume(queueName, false, deliverCallback, cancelCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收成功回调函数默认实现
     * @return 接收成功回调函数
     */
    protected DeliverCallback deliverCallbackMethod() {
        return (consumeTag, message) -> System.out.println(RECEIVE_LOG_NAME
                + "控制台打印接收到的消息" + new String(message.getBody(),
                StandardCharsets.UTF_8));
    }

    /**
     * 接收失败回调函数默认实现
     * @return 接收失败回调函数
     */
    protected CancelCallback cancelCallbackMethod() {
        return (consumeTag) -> {};
    }

    /**
     * 声明队列，实现方法：随机生成or根据队名生成
     * 绑定队列和交换机，包括BINDING_KEY的绑定
     * @return 队列名称
     */
    protected abstract String getQueue();

    /**
     * 声明交换机
     */
    protected abstract void exchangeDeclare();
}