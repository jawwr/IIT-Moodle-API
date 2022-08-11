package com.example.scheduleservice.entity.enums;

import com.example.scheduleservice.entity.DTO.WeekDTO;

public enum WeekName {
    FirstWeek,
    SecondWeek;

    public static WeekName parse(String name){
        if (name.equals("Первая неделя")){
            return FirstWeek;
        }
        return SecondWeek;
    }

    public static String parse(WeekName weekName){
        if (weekName == FirstWeek){
            return "Первая неделя";
        }
        return "Вторая неделя";
    }
}
