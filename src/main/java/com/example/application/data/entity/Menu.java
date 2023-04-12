package com.example.application.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "menu")
@Data
@NoArgsConstructor
public class Menu extends AbstractEntity {
    private String name;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Meal> meals;
    private MealType type;
    private Long orderId;
    // todo later an entity can be
    private String restaurant;

}
