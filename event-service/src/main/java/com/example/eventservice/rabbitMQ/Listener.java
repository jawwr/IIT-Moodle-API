package com.example.eventservice.rabbitMQ;

import com.example.eventservice.entity.Event;
import com.example.eventservice.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableRabbit
public class Listener {
    private final EventRepository repository;

    @Autowired
    public Listener(EventRepository repository) {
        this.repository = repository;
    }

//    @RabbitListener(queues = "eventQueueRepo")
    public void listen(String message){
        try{
            var mapper = new ObjectMapper();
            CollectionType javaType = mapper.getTypeFactory()
                    .constructCollectionType(List.class, Event.class);
            List<Event> events = mapper.readValue(message, javaType);
            repository.saveAll(events);
        }catch (Exception e){
            e.printStackTrace();//TODO добавить логгер
        }
    }
}
