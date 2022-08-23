package com.example.eventservice.rabbitMQ;

import com.example.eventservice.entity.Event;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableRabbit
public class Listener {
    @RabbitListener(queues = "myQueue")//TODO поменять имя очереди
    public void listen(String events){//TODO сделать сериализацию
        System.out.println("получено сообщение");
//        for (var event: events){
//            System.out.println(event.getEventName());
//        }
    }
}
