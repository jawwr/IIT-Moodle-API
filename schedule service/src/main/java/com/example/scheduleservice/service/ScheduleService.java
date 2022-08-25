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

/**
 * Сервис для работы с расписанием
 */
@Service
public class ScheduleService {
    private final ScheduleRepository repository;
    private final AmqpTemplate template;

    @Autowired
    public ScheduleService(ScheduleRepository repository, AmqpTemplate template) {
        this.repository = repository;
        this.template = template;
    }

    /**
     * Метод получения расписания
     * @param login
     * @return {@link ScheduleDTO}
     * @throws UserDoesNotExistException
     */
    public ScheduleDTO getSchedule(String login) throws UserDoesNotExistException {
        Map<String, String> user = getUser(login);//получения данных пользователя
        if (user.get("id").equals("null")){//если данные дефолтные, значит пользователя не существует
            throw new UserDoesNotExistException();
        }
        var groupName = user.get("groupName");//получение группы пользователя
        Schedule schedule = repository.findByGroupName(groupName);//поиск в бд
        return new ScheduleDTO(schedule);
    }

    /**
     * Метод для создания расписаиня
     * @param scheduleDTO
     */
    public void createSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule(scheduleDTO);
        repository.save(schedule);
    }

    /**
     * Метод получения пользователя из сервиса с пользователями
     * @param login
     * @return {@link Map}
     */
    private Map<String, String> getUser(String login) {
        //отправка сообщения сеервису
        template.convertAndSend("user_service_exchange", "user_service_key", new RabbitMessage(login));
        ObjectMapper mapper = new ObjectMapper();
        Object receive = null;
        while (receive == null) {
            receive = template.receiveAndConvert(RabbitConfig.QUEUE_NAME);//цикл вечный, пока сообщение не будетт получено
        }//из-за конвертера сообщение сообщение с данными пользователя может прийти как в виде мапы, так и в виде строки
        //для этого проверка на то, в каком виде пришло сообщение
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
