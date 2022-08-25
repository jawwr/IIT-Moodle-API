package com.example.scheduleservice.service;

import com.example.scheduleservice.config.RabbitConfig;
import com.example.scheduleservice.entity.DTO.ScheduleDTO;
import com.example.scheduleservice.entity.Schedule;
import com.example.scheduleservice.exceptions.UserDoesNotExistException;
import com.example.scheduleservice.rabbitmq.RabbitMessage;
import com.example.scheduleservice.repository.ScheduleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ScheduleService {
    private final ScheduleRepository repository;
    private final AmqpTemplate template;

    @Autowired
    public ScheduleService(ScheduleRepository repository, AmqpTemplate template) {
        this.repository = repository;
        this.template = template;
    }

    public ScheduleDTO getScheduleByGroupName(String login) throws UserDoesNotExistException {
        Map<String, String> user = getUser(login);
        if (user.get("id").equals("null")){
            throw new UserDoesNotExistException();
        }
        var groupName = user.get("groupName");
        Schedule schedule = repository.findByGroupName(groupName);
        return new ScheduleDTO(schedule);
    }

    public void createSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule(scheduleDTO);
        repository.save(schedule);
    }

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
}
