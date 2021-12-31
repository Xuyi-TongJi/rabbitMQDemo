package edu.seu.rabbitmqdemo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import edu.seu.rabbitmqdemo.utils.RabbitMQUtils;
import org.junit.jupiter.api.Test;

import java.util.NavigableSet;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class TestClass {
    @Test
    public void method01() {
        ConcurrentSkipListMap<String, String> map = new ConcurrentSkipListMap<>();
        map.put("1", "1");
        map.put("2", "2");
        map.put("3", "3");
        map.put("4", "4");
        map.put("5", "5");
        ConcurrentNavigableMap<String, String> subMap =
                map.headMap("1", true);
        subMap.clear();
        NavigableSet<String> strings = map.keySet();
        for (String str :
                strings) {
            System.out.println(map.get(str));
        }
    }

    @Test
    public void method02() {
        Channel channel = RabbitMQUtils.getProducerChannel();
        Channel channel1 = RabbitMQUtils.getProducerChannel();
        System.out.println(channel1 == channel);
    }
}
