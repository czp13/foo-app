package com.example.application.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "meal")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meal extends AbstractEntity {
    private String name;

    private String ingredients;

    private String description;

    private String image;

    private List<DietAttributes> dietAttributes;
}
