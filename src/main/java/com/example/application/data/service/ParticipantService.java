package com.example.application.data.service;

import com.example.application.data.dto.UserChoiceDto;
import com.example.application.data.entity.DietAttributes;
import com.example.application.data.entity.Meal;
import com.example.application.data.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParticipantService {
    public List<UserChoiceDto> getChoices(User user) {
        return generateRandomEventMealDates();
    }

    public void saveUserChoices(List<UserChoiceDto> userChoiceDtos) {
    }

    private List<UserChoiceDto> generateRandomEventMealDates() {
        List<UserChoiceDto> userChoiceDtoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserChoiceDto userChoiceDto = new UserChoiceDto();
            userChoiceDto.setEventDateTime(LocalDateTime.now().plusDays(i).toLocalDate().atTime(12, 0));
            userChoiceDto.setDeadlineDateTime(LocalDateTime.now().plusDays(i-5).toLocalDate().atTime(12, 0));
            userChoiceDto.setEventName("Event Name" + i);
            userChoiceDto.setEventDescription("Event Description" + i);
            userChoiceDto.setOrganizer(new User());
            userChoiceDto.setParticipant(new User());
            userChoiceDto.setMenuName("Menu Name" + i);
            userChoiceDto.setMenuId(1L);

            Meal glutenFreePizza = new Meal();
            glutenFreePizza.setName("Gluten Free Pizza" + i);
            glutenFreePizza.setIngredients("Gluten Free Pizza Ingredients" + i);
            glutenFreePizza.setDescription("Gluten Free Pizza Description" + i);
            glutenFreePizza.setImage("Gluten Free Pizza Image URL" + i);
            glutenFreePizza.setDietAttributes(List.of(DietAttributes.GLUTEN_FREE));

            Meal veganBurger = new Meal();
            veganBurger.setName("Vegan Burger" + i);
            veganBurger.setIngredients("Vegan Burger Ingredients" + i);
            veganBurger.setDescription("Vegan Burger Description" + i);
            veganBurger.setImage("Vegan Burger Image URL" + i);
            veganBurger.setDietAttributes(List.of(DietAttributes.VEGAN));

            userChoiceDto.setPotentialMealName(List.of(glutenFreePizza.getName(), veganBurger.getName()));

            userChoiceDtoList.add(userChoiceDto);
        }

        return userChoiceDtoList;
    }
}
