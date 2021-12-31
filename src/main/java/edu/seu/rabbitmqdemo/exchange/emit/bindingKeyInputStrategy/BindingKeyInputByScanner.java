package edu.seu.rabbitmqdemo.exchange.emit.bindingKeyInputStrategy;

import edu.seu.rabbitmqdemo.utils.ScannerUtils;

public class BindingKeyInputByScanner implements BindingKeyInputStrategy{
    @Override
    public String getBindingKey() {
        System.out.println("请输入BINDING_KEY");
        return ScannerUtils.getString();
    }
}
