package com.example.userservice.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RabbitMessage {//специальный класс для обмена сообщениями между микросервисами
    private String message;//само сообщение (чаще всего используется для передачи логина пользователя)
    private String exchange;//точка обмена (в ней приходит точка обменна отправителя)
    private String key;//ключ очееди для того, чтобы вернуть  сообщение отправителю
}
