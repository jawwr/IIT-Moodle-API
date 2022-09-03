package com.example.userservice.rabbitmq;

import com.example.userservice.entity.User;
import com.example.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@EnableRabbit
@Slf4j
public class Listener {
    private final UserService service;
    private final AmqpTemplate template;

    @Autowired
    public Listener(UserService service, AmqpTemplate template) {
        this.service = service;
        this.template = template;
    }

    @RabbitListener(queues = "userQueue")//слушает очередь для обмена данными пользователя
    public void getUserByLogin(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            RabbitMessage rabbitMessage = mapper.readValue(message, RabbitMessage.class);///парсит полученное сообшение в специальный класс
            log.info("Message receive from: " + rabbitMessage.getExchange() + " " + "Time: " + LocalTime.now());
            var login = rabbitMessage.getMessage();//парсит все нужные данные
            var exchange = rabbitMessage.getExchange();
            var key = rabbitMessage.getKey();
            User user = service.getUserByLogin(login); //поиск юзера, логин которого пришел в сообщении

            template.convertAndSend(exchange,key, user.toString());//отправка сообщения обратно отправителю
            System.out.println("Message has been sent\nUser login: " + user.getLogin());
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
//    @RabbitListener(queues = "userQueueService")
    public void reOverloaded(String message) {
        System.out.println(message);
    }
}
