package com.example.scheduleservice.rabbitmq;

import com.example.scheduleservice.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
public class Listener {
//    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void test(String l){
        System.out.println("schedule service" + l);
    }
}
