package com.example.eventservice.service;

import com.example.eventservice.entity.Event;
import com.example.eventservice.repository.EventRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final AmqpTemplate template;
    private LocalDateTime lastParse;

    @Autowired
    public EventServiceImpl(EventRepository repository, AmqpTemplate template) {
        this.repository = repository;
        this.template = template;
        lastParse = LocalDateTime.now().minusHours(3);
    }

    @Override
    public List<Event> getEventsByGroupName(String group) {
        parseEvent("{\"login\":\"kachesov03@mail.ru\",\"password\":\"Senya2003\"}");//TODO насрать еще одним классом с данными, потом изменить на получение из сервиса пользователей
        var a = template.receiveAndConvert("eventQueueRepo");
        System.out.println(a);
        if (!repository.existsByGroupName(group) && (LocalDateTime.now().getHour() - lastParse.getHour() > 6)) {
            try {
                parseEvent("{\"login\":\"kachesov03@mail.ru\",\"password\":\"Senya2003\"}");//TODO сделать запрос на данные из сервиса пользователей
                Thread.sleep(5000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return repository.findAllByGroupName(group);
    }
    private void parseEvent(String credentials) {
        template.convertAndSend("eventQueueParser", credentials);
        template.convertAndSend("userInfoQueue", "user");
        template.convertAndSend("marksQueue", "parse marks");
    }

    @Scheduled(cron = "*/10 * */12 * * *")
    private void parseEvents() {
        lastParse = LocalDateTime.now();
        var groups = repository.findAllGroupName();
        for (var group : groups) {
            template.convertAndSend("eventQueue", group);
            System.out.println("add to queue");
        }
    }
}
