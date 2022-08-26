package com.example.markservice.rabbitmq;

import com.example.markservice.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
public class Listener {
//    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void receive(String message){
        System.out.println("Message receive: " + message);
    }
}
