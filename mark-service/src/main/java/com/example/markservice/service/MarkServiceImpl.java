package com.example.markservice.service;

import com.example.markservice.config.RabbitConfig;
import com.example.markservice.entity.Marks;
import com.example.markservice.exception.UserDoesNotExistException;
import com.example.markservice.rabbitmq.RabbitMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MarkServiceImpl implements MarkService {
    private final RabbitTemplate template;

    @Autowired
    public MarkServiceImpl(RabbitTemplate template) {
        this.template = template;
    }

    @Override
    public List<Marks> getMarks(String login) throws UserDoesNotExistException {
        Map<String, String> user = getUserFromQueue(login);
        if (user.get("id").equals("null")) {
            throw new UserDoesNotExistException();
        }
        return getMarksFromQueue(user);
    }

    private List<Marks> getMarksFromQueue(Map<String, String> credentials) {
        sendMessageToMarksParser(credentials);
        return receiveMarksFromQueue();
    }

    private List<Marks> receiveMarksFromQueue() {
        ObjectMapper mapper = new ObjectMapper();
        Object receive = template.receiveAndConvert(RabbitConfig.QUEUE_NAME, 10000L);
        String receiveMessage = receive.toString();
        List<Marks> marks = new ArrayList<>();
        CollectionType javaType = mapper.getTypeFactory()
                .constructCollectionType(List.class, Marks.class);
        try {
            marks = mapper.readValue(receiveMessage, javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return marks;
    }

    private void sendMessageToMarksParser(Map<String, String> credentials) {
        template.convertAndSend("marks_parser_exchange","marks_parser_key", credentials);
    }

    private Map<String, String> getUserFromQueue(String login) {
        sendMessageToUserService(login);//отправка сообщения сервису с пользователями
        return receiveUserFromQueue();//получение данных пользователя из очереди
    }

    private Map<String, String> receiveUserFromQueue() {
        ObjectMapper mapper = new ObjectMapper();
        Object receive = null;
        receive = template.receiveAndConvert(RabbitConfig.QUEUE_NAME, 10000L);
        //из-за конвертера сообщений сообщение с данными пользователя может прийти как в виде мапы, так и в виде строки
        //для этого проверка на то, в каком виде пришло сообщение
        if (receive instanceof Map) {
            return (Map<String, String>) receive;
        }
        String receiveMessage = receive.toString();
        Map<String, String> user = new HashMap();
        try {
            user = mapper.readValue(receiveMessage, Map.class);
        } catch (Exception e) {
            e.printStackTrace();//TODO добавить логгер
        }
        return user;
    }

    private void sendMessageToUserService(String login) {
        template.convertAndSend("user_service_exchange", "user_service_key", new RabbitMessage(login));
    }
}
