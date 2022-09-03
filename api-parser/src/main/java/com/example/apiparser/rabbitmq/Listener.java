package com.example.apiparser.rabbitmq;

import com.example.apiparser.parser.Parser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@EnableRabbit
@Slf4j
public class Listener {
    private final Parser parser;
    private final AmqpTemplate template;

    @Autowired
    public Listener(Parser parser, AmqpTemplate template) {
        this.parser = parser;
        this.template = template;
    }

    @RabbitListener(queues = "eventQueueParser")
    public void parseEvents(String message){
        try{
            ObjectMapper mapper = new ObjectMapper();

            Map<String, String> credentials = mapper.readValue(message, Map.class);
            var events = parser.parseEvents(credentials.get("login"), credentials.get("password"), credentials.get("groupName"));

            template.convertAndSend("eventQueue", events);
        }catch (Exception e){
            log.info(e.getMessage());
        }
    }

    @RabbitListener(queues = "userQueueParser")
    public void parseUserInfo(String message){
        try{
            ObjectMapper mapper = new ObjectMapper();

            Map<String, String> credentials = mapper.readValue(message, Map.class);
            var userInfo = parser.parseUserInfo(credentials.get("login"), credentials.get("password"));

            template.convertAndSend("userQueueService", userInfo.toString());
        }catch (Exception e){
            log.info(e.getMessage());
        }
    }

    @RabbitListener(queues = "marksQueueParser")
    public void parseMarks(String message){
        try{
            ObjectMapper mapper = new ObjectMapper();

            Map<String, String> credentials = mapper.readValue(message, Map.class);
            var marks = parser.parseMarks(credentials.get("login"), credentials.get("password"));

            template.convertAndSend("marksQueue", marks);
            log.info("Message has been sent");
        }catch (Exception e){
            log.info(e.getMessage());
        }
    }
}
