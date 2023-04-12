package com.example.application.repository;

import com.example.application.data.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {

    @Override
    List<Meal> findAll();
    List<Meal> findByMenuId(Long menuId);
}
