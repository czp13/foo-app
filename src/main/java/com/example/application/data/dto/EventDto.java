package com.example.application.data.dto;

import com.example.application.data.entity.EventMealDate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EventDto{
    private String name;
    private String description;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LocalDate deadLine;
    private List<EventMealDto> eventMeals = new ArrayList<>();



}
