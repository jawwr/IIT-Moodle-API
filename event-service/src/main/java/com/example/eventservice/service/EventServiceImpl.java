package com.example.eventservice.service;

import com.example.eventservice.config.RabbitConfig;
import com.example.eventservice.entity.Event;
import com.example.eventservice.exceptions.UserDoesNotExistException;
import com.example.eventservice.rabbitmq.RabbitMessage;
import com.example.eventservice.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Сервис для работы с событиями
 */
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final AmqpTemplate template;
    private LocalDateTime lastParse;

    @Autowired
    public EventServiceImpl(EventRepository repository, AmqpTemplate template) {
        this.repository = repository;
        this.template = template;
        lastParse = LocalDateTime.now();
    }

    /**
     * Получение списка событитй по логину пользователя
     * @param login
     * @return {@link List}
     * @throws UserDoesNotExistException
     */
    @Override
    public List<Event> getEvents(String login) throws UserDoesNotExistException {
        Map<String, String> userCredentials = getUser(login);//получение данных пользователя
        if (userCredentials.get("id").equals("null")) {//если пользователя не существует, то выбрасывается исключение
            throw new UserDoesNotExistException();
        }
        String group = userCredentials.get("groupName");
        if (!repository.existsByGroupName(group) || (LocalDateTime.now().getHour() - lastParse.getHour() > 6)) {
            try {
                parseEvent(userCredentials);
                var eventList = receiveEvents();
                repository.saveAll(eventList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return repository.findAllByGroupName(group);
    }

    /**
     * Метод получения юзера из сервиса с пользователями
     * @param login
     * @return {@link Map}
     */
    private Map<String, String> getUser(String login) {
        template.convertAndSend("user_service_exchange", "user_service_key", new RabbitMessage(login));

        ObjectMapper mapper = new ObjectMapper();
        Object receive = null;
        while (receive == null) {
            receive = template.receiveAndConvert(RabbitConfig.QUEUE_NAME);
        }
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

    /**
     * Метод получения ссписка событий из парсера
     * @return {@link List}
     */
    private List<Event> receiveEvents() {
        try {
            List message = (List) template.receiveAndConvert(RabbitConfig.QUEUE_KEY);
            if (message == null) {
                return new ArrayList<>();
            }
            List<Event> events;
            var mapper = new ObjectMapper();
            CollectionType javaType = mapper.getTypeFactory()
                    .constructCollectionType(List.class, Event.class);
            events = mapper.readValue(message.toString(), javaType);
            return events;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Метод отправки сообщения для парсинга событий
     * @param credentials
     */
    private void parseEvent(Map<String, String> credentials) {
        template.convertAndSend("event_parser_exchange", "event_parser_key", credentials);
    }

    /**
     * Метод для парсинга событий
     */
    @Scheduled(cron = "*/10 * */12 * * *")
    private void parseEvents() {
        lastParse = LocalDateTime.now();
        var groups = repository.findAllGroupName();//TODO доделать получение всех событитй и сохранение их в бд
        for (var group : groups) {
            template.convertAndSend("eventQueue", group);
            System.out.println("add to queue");//TODO добавить логгер
        }
    }
}
