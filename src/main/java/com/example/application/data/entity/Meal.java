package com.example.application.data.entity;

import jakarta.persistence.*;
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

    private Long price;

    @ManyToOne
    private Menu menu;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<DietAttributes> dietAttributes;
}
