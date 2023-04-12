package com.example.application.data.dto;

import com.example.application.data.entity.Meal;
import com.example.application.data.entity.Menu;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EventMealDto {
    private String name;
    private String description;
    private LocalDate date;
    private Menu menu;
    private Meal selectedMeal;
    private String note;

    public EventMealDto(String name, String description, LocalDate date, Menu menu) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.menu = menu;
    }
}
