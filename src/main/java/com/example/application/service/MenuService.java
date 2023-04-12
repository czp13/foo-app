package com.example.application.service;

import com.example.application.data.entity.Meal;
import com.example.application.data.entity.Menu;
import com.example.application.repository.MealRepository;
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
    private final MealRepository mealRepository;


    public List<Meal> findByMenuId(Long menuId){
        return mealRepository.findByMenuId(menuId);
    }
    public List<Menu> findAll(){
        return menuRepository.findAll();
    }
    public Menu save(Menu menuToSave){
        //TODO check..
        return menuRepository.save(menuToSave);
    }

    /**
     * Saves meal to menu(found by menu id) and return meals of the menu
     * @param meal
     * @param menuId
     * @return
     */
    public List<Meal> save(Meal meal, Long menuId){
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new IllegalStateException("Menu is not found."));
        meal.setMenu(menu);
        mealRepository.save(meal);
        return mealRepository.findByMenuId(menuId);
    }

    public void deleteById(Long menuId){
        menuRepository.deleteById(menuId);
    }

    public Meal update(Meal meal) {
        return mealRepository.save(meal);
    }
}
