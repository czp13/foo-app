package com.example.application.data.entity;

public enum MealType {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DESSERT("Dessert");

    private final String name;

    MealType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
