package com.example.application.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "meal")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meal extends AbstractEntity {
    private String name;

    private String ingredients;

    private String image;

    private Long price;

    @ManyToOne
    private Menu menu;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<DietAttributes> dietAttributes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Meal meal = (Meal) o;
        return Objects.equals(name, meal.name) && Objects.equals(ingredients, meal.ingredients) && Objects.equals(image, meal.image) && Objects.equals(price, meal.price) && Objects.equals(menu, meal.menu) && Objects.equals(dietAttributes, meal.dietAttributes);
    }

    @Override
    public String toString() {
        return "Meal{" +
                "name='" + name + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", dietAttributes=" + dietAttributes +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), name, ingredients, image, price, dietAttributes);
    }
}
