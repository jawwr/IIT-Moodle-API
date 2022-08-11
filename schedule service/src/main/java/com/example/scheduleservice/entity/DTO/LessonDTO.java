package com.example.scheduleservice.entity.DTO;

import com.example.scheduleservice.entity.Lesson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonDTO {
    private String name;
    private String teacher;
    private String auditorium;
    private String timeStart;
    private String timeEnd;

    public LessonDTO(Lesson lesson){
        this.name = lesson.getName();
        this.auditorium = lesson.getAuditorium();
        this.timeStart = lesson.getTimeStart();
        this.teacher = lesson.getTeacher();
        this.timeEnd = lesson.getTimeEnd();
    }
}
