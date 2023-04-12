package com.example.application.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "event")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event extends AbstractEntity {
    private String name;
    private String description;
    private List<User> participants;
    private List<User> organizers;
    private List<EventMealDate> meals;
    private LocalDate from;
    private LocalDate to;
    private LocalDate deadline;
}
