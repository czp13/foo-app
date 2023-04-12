package com.example.application.data.service;

import com.example.application.data.entity.EventMealDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EventMailDateRepository extends JpaRepository<EventMealDate, Long>, JpaSpecificationExecutor<EventMealDate> {
    public EventMealDate findByName(String username);
}
