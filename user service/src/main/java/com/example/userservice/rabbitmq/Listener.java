package com.example.userservice.rabbitmq;

import com.example.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@EnableRabbit
public class Listener {
    private final UserService service;
    private final AmqpTemplate template;

    @Autowired
    public Listener(UserService service, AmqpTemplate template) {
        this.service = service;
        this.template = template;
    }

    @RabbitListener(queues = "userQueue")
    public void getUserByLogin(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            RabbitMessage rabbitMessage = mapper.readValue(message, RabbitMessage.class);
            var login = rabbitMessage.getMessage();
            var exchange = rabbitMessage.getExchange();
            var key = rabbitMessage.getKey();
            var user = service.getUserByLogin(login);
            template.convertAndSend(exchange,key, user.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
