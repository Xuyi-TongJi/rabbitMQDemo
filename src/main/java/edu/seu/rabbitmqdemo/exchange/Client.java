package edu.seu.rabbitmqdemo.exchange;

import edu.seu.rabbitmqdemo.exchange.emit.impl.DirectEmitLog;
import edu.seu.rabbitmqdemo.exchange.emit.impl.FanoutEmitLog;
import edu.seu.rabbitmqdemo.exchange.emit.impl.TopicEmitLog;
import edu.seu.rabbitmqdemo.exchange.receive.impl.FanoutReceiveLog;
import edu.seu.rabbitmqdemo.exchange.receive.impl.DirectReceiveLog;
import edu.seu.rabbitmqdemo.exchange.receive.impl.TopicReceiveLog;

import java.util.ArrayList;
import java.util.List;

public class Client {
    public static void main(String[] args) {

    }

    private static void fanout() {
        String EXCHANGE_NAME = "EXCHANGE_NAME";
        // 接收方声明了交换机，先定义
        new Thread(new FanoutReceiveLog("RECEIVE1", EXCHANGE_NAME)).start();
        new Thread(new FanoutReceiveLog("RECEIVE2", EXCHANGE_NAME)).start();
        new Thread(new FanoutEmitLog("EMIT", EXCHANGE_NAME)).start();
    }

    private static void direct() {
        String EXCHANGE_NAME = "EXCHANGE_NAME2";
        String QUEUE_1 = "QUEUE_11";
        String QUEUE_2 = "QUEUE_22";
        List<String> BindKeys1 = new ArrayList<>();
        BindKeys1.add("warning");
        BindKeys1.add("info");
        List<String> BindKeys2 = new ArrayList<>();
        BindKeys2.add("error");
        BindKeys2.add("info");
        new Thread(new DirectReceiveLog(EXCHANGE_NAME, "Receive1", QUEUE_1, BindKeys1)).start();
        new Thread(new DirectReceiveLog(EXCHANGE_NAME, "Receive2", QUEUE_2, BindKeys2)).start();
        new Thread(new DirectEmitLog("EMIT", EXCHANGE_NAME)).start();
    }

    private static void topic() {
        String EXCHANGE_NAME = "EXCHANGE_NAME3";
        String QUEUE_1 = "QUEUE_11";
        String QUEUE_2 = "QUEUE_22";
        List<String> BindKeys1 = new ArrayList<>();
        BindKeys1.add("*.orange.*");
        List<String> BindKeys2 = new ArrayList<>();
        BindKeys2.add("*.*.rabbit");
        BindKeys2.add("lazy.#");
        new Thread(new TopicReceiveLog(EXCHANGE_NAME, "Receive1", QUEUE_1, BindKeys1)).start();
        new Thread(new TopicReceiveLog(EXCHANGE_NAME, "Receive2", QUEUE_2, BindKeys2)).start();
        new Thread(new TopicEmitLog("EMIT", EXCHANGE_NAME)).start();
    }

}
