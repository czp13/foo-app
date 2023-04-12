package com.example.application.data.entity;

import jakarta.persistence.Entity;
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
    private List<Meal> meals;
    // todo later an entity can be
    private String restaurant;
}
