package com.example.application.data.service;

import com.example.application.data.entity.Event;
import com.example.application.data.entity.EventMealDate;
import com.example.application.data.entity.Meal;
import com.example.application.data.entity.MealType;
import com.example.application.data.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository repository;

    @Autowired
    private EventMailDateRepository mealDateRepository;

    public List<Event> getEvents() {
        return repository.findAll();
    }

    public void save(Event event) {
        repository.save(event);
    }

    public Optional<Event> getEventById(Long eventId) {
        return repository.findById(eventId);
    }

    public Map<LocalDate, List<EventMealDate>> getMealDatesByEvent(Event event) {
        Map<LocalDate, List<EventMealDate>> mealDatesByDate = new HashMap<>();
        List<LocalDate> eventDays = event.getFromDate().datesUntil(event.getToDate().plusDays(1))
                .collect(Collectors.toList());
        for (LocalDate date : eventDays) {
            EventMealDate emd = new EventMealDate();
            emd.setDate(date);
            emd.setEvent(event);
            emd.setName("EMD");
            emd.setDescription("EMD description");
            Menu menu = new Menu();
            menu.setName("Breakfast");
            menu.setType(MealType.BREAKFAST);
            menu.setRestaurant("Breakfast Restaurant");
            List<Meal> meals = new ArrayList<>();
            Meal meal = new Meal("Mealname", "Incs", "Desc", "", 12l, menu, new ArrayList<>());
            Meal meal2 = new Meal("Mealname", "Incs", "Desc", "", 12l, menu, new ArrayList<>());
            meals.add(meal);
            meals.add(meal2);
            menu.setMeals(meals);
            emd.setMenu(menu);
            if (mealDatesByDate.containsKey(date)) {
                mealDatesByDate.get(date).add(emd);
            } else {
                List<EventMealDate> list = new ArrayList<>();
                list.add(emd);
                mealDatesByDate.put(date, list);
            }


            emd = new EventMealDate();
            emd.setDate(date);
            emd.setEvent(event);
            emd.setName("EMD 2");
            emd.setDescription("EMD 2 description");
            menu = new Menu();
            menu.setName("Lunch");
            menu.setType(MealType.LUNCH);
            menu.setRestaurant("Lunch Restaurant");
            meals = new ArrayList<>();
            meal = new Meal("Mealname", "Incs", "Desc", "", 12l, menu, new ArrayList<>());
            meal2 = new Meal("Mealname", "Incs", "Desc", "", 12l, menu, new ArrayList<>());
            meals.add(meal);
            meals.add(meal2);
            menu.setMeals(meals);
            emd.setMenu(menu);

            if (mealDatesByDate.containsKey(date)) {
                mealDatesByDate.get(date).add(emd);
            } else {
                List<EventMealDate> list = new ArrayList<>();
                list.add(emd);
                mealDatesByDate.put(date, list);
            }
        }
        return mealDatesByDate;
    }
}
