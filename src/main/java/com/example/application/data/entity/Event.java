package com.example.application.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "vaadin_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event extends AbstractEntity {
    private String name;
    private String description;
    @ManyToMany
    @JoinTable(
            name = "event_participants",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    private List<User> participants;
    @ManyToMany
    @JoinTable(
            name = "event_organizers",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "organizer_id")
    )
    private List<User> organizers;
    @ManyToMany
    @JoinTable(
            name = "event_meals",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "event_meal_date_id")
    )
    private List<EventMealDate> eventMealDates;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LocalDate deadline;
}
