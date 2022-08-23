package com.example.eventservice.service;

import com.example.eventservice.repository.EventRepository;
import com.example.eventservice.entity.Event;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService{
    private final EventRepository repository;
    private final AmqpTemplate template;

    @Autowired
    public EventServiceImpl(EventRepository repository, AmqpTemplate template) {
        this.repository = repository;
        this.template = template;
    }

    @Override
    public List<Event> getEventsByGroupName(String group) {
        addQueue(group);//TODO убрать
        return repository.findAllByGroupName(group);
    }

    private void addQueue(String message){
        template.convertAndSend("eventQueue",message);
    }

    @Scheduled(cron = "*/10 * */12 * * *")
    private void parseEvents(){
        var groups = repository.findAllGroupName();
        for (var group : groups){
            template.convertAndSend("eventQueue",group);
            System.out.println("add to queue");
        }
    }
}
