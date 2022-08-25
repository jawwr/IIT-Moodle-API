package com.example.userservice.rabbitmq;

import com.example.userservice.entity.User;
import com.example.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

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

    @RabbitListener(queues = "userQueue")//слушает очередь для обмена даннымипользователя
    public void getUserByLogin(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            RabbitMessage rabbitMessage = mapper.readValue(message, RabbitMessage.class);///парсит полученное сообшение в специальный класс
            System.out.println("Message receive from: " + rabbitMessage.getExchange() + " " + "Time: " + LocalTime.now());//TODO добавить логгер
            var login = rabbitMessage.getMessage();//парсит все нужные данные
            var exchange = rabbitMessage.getExchange();
            var key = rabbitMessage.getKey();
            var user = service.getUserByLogin(login);//поиск юзера, логин которого пришел в сообщении
            if(user == null){//если юзер не найден, то он становится дефолтным
                user = new User();
            }

            template.convertAndSend(exchange,key, user.toString());//отправка сообщения обратно отправителю
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
