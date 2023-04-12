package com.example.application.repository;

import com.example.application.data.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Override
    List<Menu> findAll();

}
