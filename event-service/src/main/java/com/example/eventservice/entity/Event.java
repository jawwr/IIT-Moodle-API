package com.example.eventservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "group_name")
    private String groupName;
    @Column(name = "event_name")
    private String eventName;
    @Column(name = "date_end")
    private Date date;
    @Column(name = "lesson_name")
    private String lessonName;
}
