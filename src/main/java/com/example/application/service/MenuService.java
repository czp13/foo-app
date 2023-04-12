package com.example.application.service;

import com.example.application.data.entity.Meal;
import com.example.application.data.entity.Menu;
import com.example.application.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuService {
    private final MenuRepository menuRepository;

    public List<Menu> findAll(){
        return menuRepository.findAll();
    }
    public Menu save(Menu menuToSave){
        //TODO check..
        return menuRepository.save(menuToSave);
    }

    public Menu save(Meal meal, Long menuId){
        return null;
    }

    public void deleteById(Long menuId){
        menuRepository.deleteById(menuId);
    }

}
