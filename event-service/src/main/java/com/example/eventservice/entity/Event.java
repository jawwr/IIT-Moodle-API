package com.example.eventservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ApiModel(value = "Event", description = "Event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "group_name")
    @ApiModelProperty(notes = "Group name of the Student",required=true,value="test name")
    private String groupName;
    @Column(name = "event_name")
    private String eventName;
    @Column(name = "date_end")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate date;
    @Column(name = "lesson_name")
    private String lessonName;

    public Event(String groupName, String eventName, LocalDate date, String lessonName) {
        this.groupName = groupName;
        this.eventName = eventName;
        this.date = date;
        this.lessonName = lessonName;
    }
}
