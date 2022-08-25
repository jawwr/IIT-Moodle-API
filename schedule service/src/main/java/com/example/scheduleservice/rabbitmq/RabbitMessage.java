package com.example.scheduleservice.rabbitmq;

import com.example.scheduleservice.config.RabbitConfig;
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
