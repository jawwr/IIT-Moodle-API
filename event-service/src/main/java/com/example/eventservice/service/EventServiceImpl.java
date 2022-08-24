package com.example.eventservice.service;

import com.example.eventservice.entity.Event;
import com.example.eventservice.entity.RabbitMessage;
import com.example.eventservice.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final AmqpTemplate template;
    private LocalDateTime lastParse;

    @Autowired
    public EventServiceImpl(EventRepository repository, AmqpTemplate template) {
        this.repository = repository;
        this.template = template;
        lastParse = LocalDateTime.now().minusHours(3);
    }

    @Override
    public List<Event> getEventsByGroupName(String login) {
        var userCredentials = getUser(login);
        var group = userCredentials.get("groupName");
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

    private Map<String, String> getUser(String login) {
        var message = new RabbitMessage(login);
        template.convertAndSend("user_service_exchange", "user_service_key", message);
        return (Map<String, String>) template.receiveAndConvert("event_service_key");
    }

    private List<Event> receiveEvents() {
        try {
            List message = (List) template.receiveAndConvert("event_service_key");
            if (message == null) {
                return new ArrayList<>();
            }
            List<Event> events = new ArrayList<>();
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

    private void parseEvent(Map<String, String> credentials) {
        template.convertAndSend("event_parser_exchange", "event_parser_key", credentials);
    }

    @Scheduled(cron = "*/10 * */12 * * *")
    private void parseEvents() {
        lastParse = LocalDateTime.now();
        var groups = repository.findAllGroupName();
        for (var group : groups) {
            template.convertAndSend("eventQueue", group);
            System.out.println("add to queue");//TODO добавить логгер
        }
    }
}
