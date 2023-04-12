package com.example.application.data.entity;

// TODO: rename later to better name

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_meal_choice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMealChoice extends AbstractEntity {
    @OneToOne
    private User user;
    @OneToOne
    private Meal meal;

    private String note;
}
