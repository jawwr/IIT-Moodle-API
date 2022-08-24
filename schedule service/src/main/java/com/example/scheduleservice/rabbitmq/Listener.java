package com.example.scheduleservice.rabbitmq;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
public class Listener {
//    @RabbitListener(queues = "userQueue")
    public void test(String l){
        System.out.println("schedule service" + l);
    }
}
