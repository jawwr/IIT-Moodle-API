package com.example.scheduleservice.entity;

import com.example.scheduleservice.entity.DTO.LessonDTO;
import com.example.scheduleservice.entity.enums.DayOfWeek;
import com.example.scheduleservice.entity.enums.WeekName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "lessons")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "teacher")
    private String teacher;

    @Column(name = "auditorium")
    private String auditorium;

    @Column(name = "time_start")
    private String timeStart;

    @Column(name = "time_end")
    private String timeEnd;

    @Column(name = "week")
    @Enumerated(EnumType.STRING)
    private WeekName weekName;

    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private DayOfWeek day;

    public Lesson(LessonDTO lessonDTO, WeekName week, DayOfWeek day) {
        this.name = lessonDTO.getName();
        this.teacher = lessonDTO.getTeacher();
        this.auditorium = lessonDTO.getAuditorium();
        this.timeStart = lessonDTO.getTimeStart();
        this.timeEnd = lessonDTO.getTimeEnd();
        this.weekName = week;
        this.day = day;
    }
}
