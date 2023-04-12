package com.example.application.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "menu")
@Data
@NoArgsConstructor
public class Menu extends AbstractEntity {
    private String name;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Meal> meals;
    private MealType type;
    private Long orderId;
    // todo later an entity can be
    private String restaurant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Menu menu = (Menu) o;
        return Objects.equals(name, menu.name) && Objects.equals(meals, menu.meals) && type == menu.type && Objects.equals(orderId, menu.orderId) && Objects.equals(restaurant, menu.restaurant);
    }

    @Override
    public String toString() {
        return "Menu{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", orderId=" + orderId +
                ", restaurant='" + restaurant + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), name, type, orderId, restaurant);
    }
}
