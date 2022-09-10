package com.example.scheduleservice.entity.DTO;

import com.example.scheduleservice.entity.Lesson;
import com.example.scheduleservice.entity.enums.DayOfWeek;
import com.example.scheduleservice.entity.enums.WeekName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekDTO {
    private String name;
    private List<DayDTO> days;

    public WeekDTO(List<Lesson> lessons){
        this.name = WeekName.parse(lessons.stream().findFirst().get().getWeekName());
        this.days = getDaysFromListLessons(lessons);
    }
    private List<DayDTO> getDaysFromListLessons(List<Lesson> lessons){
        List<DayDTO> dayDTOs = new ArrayList<>();
        for (var days : DayOfWeek.values()){
            var list = lessons.stream().filter(x -> x.getDay().equals(days)).collect(Collectors.toList());
            if(!list.isEmpty()){
                dayDTOs.add(new DayDTO(list));
            }
        }
        return dayDTOs;
    }
}
