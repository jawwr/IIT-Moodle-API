package com.example.scheduleservice.entity.DTO;

import com.example.scheduleservice.entity.Schedule;
import com.example.scheduleservice.entity.enums.WeekName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDTO {
    private String groupName;
    private WeekDTO firstWeek;
    private WeekDTO secondWeek;

    public ScheduleDTO(Schedule schedule){
        this.groupName = schedule.getGroupName();
        this.firstWeek = getDayByWeekName(schedule, WeekName.FirstWeek.name());
        this.secondWeek = getDayByWeekName(schedule, WeekName.SecondWeek.name());
    }
    private WeekDTO getDayByWeekName(Schedule schedule, String weekName){
        var list = schedule.getLessons().stream().filter(x -> x.getWeekName().name().equals(weekName)).collect(Collectors.toList());
        return new WeekDTO(list);
    }
}
