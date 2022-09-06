package com.example.scheduleservice.entity.DTO;

import com.example.scheduleservice.entity.Lesson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayDTO {
    private String name;
    private List<LessonDTO> lessons;

    public DayDTO(List<Lesson> lessons){
        var name = lessons.stream().findFirst();
        if(name.isEmpty()){
            return;
        }
        this.name = name.get().getDay().name();

        List<LessonDTO> lessonDTOs = new ArrayList<>();
        for (var lesson : lessons){
            lessonDTOs.add(new LessonDTO(lesson));

        }
        this.lessons = lessonDTOs;
    }
}
