package com.example.application.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "event_meal_date")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventMealDate extends AbstractEntity{
    private String name;
    private String description;
    private List<User> participants;
    private LocalDate date;
}
