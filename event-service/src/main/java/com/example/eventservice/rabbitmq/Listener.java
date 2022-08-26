package com.example.eventservice.rabbitmq;

import com.example.eventservice.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
public class Listener {//класс для разгрузки очереди (в работе неучаствует)
    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void listen(String message){
        System.out.println(message);
    }
}
