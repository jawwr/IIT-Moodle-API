package com.example.markservice.rabbitmq;

import com.example.markservice.config.RabbitConfig;
import lombok.Getter;

/**
 * Класс для обмена сообщениями между микрорсервисами
 */
@Getter
public class RabbitMessage {
    private final String message;//само сообщение (чаще всего логин пользователя для получения данных)
    private final String exchange = RabbitConfig.QUEUE_EXCHANGE;//точка обмена задана изначально
    private final String key = RabbitConfig.QUEUE_KEY;//ключ задан изначально

    public RabbitMessage(String message) {
        this.message = message;
    }
}
