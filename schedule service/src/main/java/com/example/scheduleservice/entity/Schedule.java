package com.example.scheduleservice.entity;

import com.example.scheduleservice.entity.DTO.ScheduleDTO;
import com.example.scheduleservice.entity.DTO.WeekDTO;
import com.example.scheduleservice.entity.enums.DayOfWeek;
import com.example.scheduleservice.entity.enums.WeekName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedule")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "groupName")
    private String groupName;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "schedule_id")
    private List<Lesson> lessons;

    public Schedule(ScheduleDTO scheduleDTO) {
        this.groupName = scheduleDTO.getGroupName();
        this.lessons = getLessonsFromWeek(scheduleDTO.getFirstWeek());
        this.lessons.addAll(getLessonsFromWeek(scheduleDTO.getSecondWeek()));
    }

    private List<Lesson> getLessonsFromWeek(WeekDTO week) {
        List<Lesson> lessonList = new ArrayList<>();
        WeekName nameOfWeek = WeekName.parse(week.getName());

        var days = week.getDays();
        for (var day : days) {
            DayOfWeek dayOfWeek = DayOfWeek.valueOf(day.getName());
            var lessons = day.getLessons();

            for (var lesson : lessons) {
                lessonList.add(new Lesson(lesson, nameOfWeek, dayOfWeek));
            }
        }
        return lessonList;
    }
}
