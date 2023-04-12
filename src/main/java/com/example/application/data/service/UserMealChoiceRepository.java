package com.example.application.data.service;

import com.example.application.data.entity.UserMealChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserMealChoiceRepository extends JpaRepository<UserMealChoice, Long>, JpaSpecificationExecutor<UserMealChoice> {
}
