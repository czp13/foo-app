package com.example.application.views.eventlist;

import com.example.application.data.dto.EventDto;
import com.example.application.data.dto.EventMealDto;
import com.example.application.data.entity.Event;
import com.example.application.data.entity.EventMealDate;
import com.example.application.data.entity.Meal;
import com.example.application.data.service.EventService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@PageTitle("Select Menus")
@Route(value = "select-menu", layout = MainLayout.class)
@RolesAllowed("USER")
public class EventMenuSelectionListView extends VerticalLayout {


    private final EventService eventService;
    public EventMenuSelectionListView(EventService eventService){
        this.eventService = eventService;

        List<EventDto> eventDtos = eventService.getUserEvents();

        eventDtos.forEach(event -> {
            Details eventView = createEventView(event);
            add(eventView);
        });
    }


    private Details createEventView(EventDto event){

        VerticalLayout detailSummaryLayout = new VerticalLayout();
        detailSummaryLayout.setPadding(false);

        HorizontalLayout nameLayout = new HorizontalLayout();
        nameLayout.setAlignItems(Alignment.END);
        nameLayout.add(new H4(event.getName()));
        nameLayout.add(new Span(event.getDeadLine().toString()));

        detailSummaryLayout.add(nameLayout);
        detailSummaryLayout.add(new Span(event.getDescription()));

        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.addClassName("event-content");
        contentLayout.setPadding(true);

        event.getEventMeals().forEach(eventMealDto -> {

            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.addClassName("event-menu-content");
            verticalLayout.setPadding(false);
            verticalLayout.setSpacing(false);
            Span menuTypeNameSpan = new Span(eventMealDto.getMenu().getType().getName() + " - " + eventMealDto.getMenu().getName());
            menuTypeNameSpan.addClassName(LumoUtility.FontSize.LARGE);
            menuTypeNameSpan.addClassName(LumoUtility.FontWeight.BOLD);
            verticalLayout.add(menuTypeNameSpan);
            verticalLayout.add(new Hr());

            RadioButtonGroup<Meal> mealRadioButtonGroup = new RadioButtonGroup<>();

            mealRadioButtonGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
            mealRadioButtonGroup.setItems(eventMealDto.getMenu().getMeals());
            ComponentUtil.setData(mealRadioButtonGroup, EventMealDto.class, eventMealDto);
            mealRadioButtonGroup.setRenderer(new ComponentRenderer<>(meal -> {
                VerticalLayout menuItemLayout = new VerticalLayout();
                menuItemLayout.addClassName("menu-item-layout");
                menuItemLayout.setPadding(false);
                menuItemLayout.setSpacing(false);

                Span mealNameSpan = new Span(meal.getName());
                mealNameSpan.addClassName(LumoUtility.FontSize.MEDIUM);
                mealNameSpan.addClassName(LumoUtility.FontWeight.BOLD);
                Span mealIngredientsSpan = new Span(meal.getIngredients());
                mealIngredientsSpan.addClassName(LumoUtility.FontSize.SMALL);

                menuItemLayout.add(mealNameSpan);
                menuItemLayout.add(mealIngredientsSpan);

                return menuItemLayout;
            }));
            mealRadioButtonGroup.addValueChangeListener(selectionChangeEvent -> {
                EventMealDto mealDto = ComponentUtil.getData(selectionChangeEvent.getSource(), EventMealDto.class);
                Meal value = selectionChangeEvent.getValue();
                mealDto.setSelectedMeal(value);

            });
            verticalLayout.add(mealRadioButtonGroup);
            verticalLayout.addClassName(LumoUtility.Margin.Top.LARGE);
            contentLayout.add(verticalLayout);
        });

        Button approveMenu = new Button("Approve");
        approveMenu.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        approveMenu.addClickListener(approveClickEvent -> {

        });
        contentLayout.add(approveMenu);
        Details details = new Details(detailSummaryLayout, contentLayout);
        return details;
    }
}
