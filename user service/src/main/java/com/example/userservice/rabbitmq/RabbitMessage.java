package com.example.userservice.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RabbitMessage {
    private String message;
    private String exchange;
    private String key;

    @Override
    public String toString() {
        return "{" +
                "\"message\":\"" + message + "\"," +
                "\"exchange\":\"" + exchange + "\"," +
                "\"key\":\"" + key + "\"" +
                '}';
    }
}
