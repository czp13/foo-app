package com.example.application.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    private String name;
    private String description;
    @ManyToMany
    @JoinTable(
            name = "event_meal_date_participants",
            joinColumns = @JoinColumn(name = "event_meal_date_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    private List<User> participants;

    @OneToOne
    @JoinColumn(name = "menu_id")
    // Todo: Working until the menu does not have multiple courses like starter. soup, main, dessert,
    // only ONE food is allowed!
    // Ask maybe Anna?!
    private Menu menu;

    @OneToMany
    @JoinColumn(name = "user_meal_choice_id")
    private List<UserMealChoice> userMealChoices;

    private LocalDate date;
}
