package edu.seu.rabbitmqClientDemo.concurrent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
public class Message<T> {
    private T data;
}