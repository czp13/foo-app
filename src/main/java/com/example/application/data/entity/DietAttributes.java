package com.example.application.data.entity;

// Todo: add icons as well somewhere in the application
public enum DietAttributes {
    // generate lactose-free, gluten-free, clean-eat, other enums:
    // https://www.baeldung.com/java-enum-values
    LACTOSE_FREE("Lactose free"),
    GLUTEN_FREE("Gluten free"),
    CLEAN_EAT("Clean eat"),
    VEGAN("Vegan"),

    OTHER("Other");

    private final String name;

    DietAttributes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
