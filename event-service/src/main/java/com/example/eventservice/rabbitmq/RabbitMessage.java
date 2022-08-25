package com.example.eventservice.rabbitmq;

import com.example.eventservice.config.RabbitConfig;
import lombok.Getter;

@Getter
public class RabbitMessage {
    private final String message;
    private final String exchange = RabbitConfig.QUEUE_EXCHANGE;
    private final String key = RabbitConfig.QUEUE_KEY;

    public RabbitMessage(String message) {
        this.message = message;
    }
}
