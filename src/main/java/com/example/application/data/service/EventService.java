package com.example.application.data.service;

import com.example.application.data.dto.EventDto;
import com.example.application.data.dto.EventMealDto;
import com.example.application.data.entity.Event;
import com.example.application.data.entity.EventMealDate;
import com.example.application.data.entity.Meal;
import com.example.application.data.entity.MealType;
import com.example.application.data.entity.Menu;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository repository;


    public List<Event> getEvents() {
        return repository.findAll();
    }

    public void save(Event event) {
        repository.save(event);
    }

    public Optional<Event> getEventById(Long eventId) {
        return repository.findById(eventId);
    }


    @Transactional
    public List<EventDto> getUserEvents(){
        List<Event> allEvents = repository.findAll();
        return allEvents.stream().map(event -> {
            EventDto eventDto = new EventDto();
            eventDto.setName(event.getName());
            eventDto.setDescription(event.getDescription());
            eventDto.setDeadLine(event.getDeadline());
            eventDto.setFromDate(event.getFromDate());
            eventDto.setToDate(event.getToDate());

            Map<LocalDate, List<EventMealDate>> dateListMap = getMealDatesByEvent(event);

            dateListMap.forEach((localDate, meals) -> {
                EventMealDate eventMealDate = meals.get(0);
                EventMealDto mealDto = new EventMealDto(eventMealDate.getName(), eventMealDate.getDescription(), eventMealDate.getDate(), eventMealDate.getMenu());
                eventDto.getEventMeals().add(mealDto);
            });
            eventDto.getEventMeals().sort(Comparator.comparing(EventMealDto::getDate));
            return eventDto;
        }).collect(Collectors.toList());

    }

    public Map<LocalDate, List<EventMealDate>> getMealDatesByEvent(Event event) {

        Map<LocalDate, List<EventMealDate>> mealDatesByDate = new HashMap<>();
        if (event == null) {
            return mealDatesByDate;
        }
        List<LocalDate> eventDays = event.getFromDate().datesUntil(event.getToDate().plusDays(1))
                .collect(Collectors.toList());
        for (LocalDate date : eventDays) {
            EventMealDate emd = new EventMealDate();
            emd.setDate(date);
            emd.setEvent(event);
            emd.setName("EMD");
            emd.setDescription("EMD description");
            Menu menu = new Menu();
            menu.setName("Pizza");
            menu.setType(MealType.LUNCH);
            menu.setRestaurant("Lunch Restaurant");
            List<Meal> meals = new ArrayList<>();
            Meal meal = new Meal("Porchetta e Carciofi", "Fior di Latte mozzarella, porchetta (pork belly), roasted and marinated artichokes, pecorino and parmesan mix, basil, olive oil", "", 12l, menu, new ArrayList<>());
            Meal meal2 = new Meal("Aglio, olio e peperoncino", "Fior di latte mozzarella, garlic, garlic oil, dried chili, pecorino romano & parmesan cheese, leaf parsley", "", 12l, menu, new ArrayList<>());
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
            menu.setName("Pizza 2");
            menu.setType(MealType.LUNCH);
            menu.setRestaurant("Lunch Restaurant");
            meals = new ArrayList<>();
            meal = new Meal("Porchetta e Carciofi", "Fior di Latte mozzarella, porchetta (pork belly), roasted and marinated artichokes, pecorino and parmesan mix, basil, olive oil","", 12l, menu, new ArrayList<>());
            meal2 = new Meal("Aglio, olio e peperoncino", "Fior di latte mozzarella, garlic, garlic oil, dried chili, pecorino romano & parmesan cheese, leaf parsley", "", 12l, menu, new ArrayList<>());
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
