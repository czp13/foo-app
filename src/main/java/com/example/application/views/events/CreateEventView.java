package com.example.application.views.events;

import com.example.application.data.entity.Event;
import com.example.application.data.entity.EventMealDate;
import com.example.application.data.entity.MealType;
import com.example.application.data.entity.Menu;
import com.example.application.data.service.EventService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.model.Dial;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@PageTitle("Create Event")
@Route(value = "create-event", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class CreateEventView extends VerticalLayout implements HasUrlParameter<Long> {

    private final EventService eventService;

    private Map<LocalDate, List<EventMealDate>> mealDatesByEvent;
    private DatePicker startDateField;
    private DatePicker endDateField;
    private TextField eventNameField;
    private TextField descriptionField;
    private Button saveChanges;
    private VerticalLayout eventDaysLayout;

    private Event event;
    private Binder<Event> eventBinder;
    private DatePicker selectionDeadlineField;

    public CreateEventView(EventService eventService) {
        this.eventService = eventService;
        mealDatesByEvent = eventService.getMealDatesByEvent(event);
        setMargin(true);

        HorizontalLayout header = new HorizontalLayout();
        header.setPadding(true);
        header.setWidth(100, Unit.PERCENTAGE);
        saveChanges = new Button("Save changes");
        saveChanges.addClickListener(e -> {
            try {
                eventBinder.writeBean(event);
                eventService.save(event);
                Notification.show("Saved");
            } catch (ValidationException ex) {
                Notification.show("Not saved, check that event information is correct");
            }
        });

        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        header.add(new Span("Create event"), saveChanges);
        saveChanges.getStyle().set("margin-left", "auto");

        FormLayout eventDataLayout = initEventForm();

        eventDaysLayout = new VerticalLayout();

        add(header, eventDataLayout, new Hr(), eventDaysLayout);
    }

    private FormLayout initEventForm() {
        FormLayout eventDataLayout = new FormLayout();
        eventNameField = new TextField("Event name");
        descriptionField = new TextField("Event description");
        startDateField = new DatePicker("Start date");

        startDateField.addValueChangeListener(e -> {
            datesUpdated();
        });
        endDateField = new DatePicker("End date");
        endDateField.addValueChangeListener(e -> {
            datesUpdated();
        });
        selectionDeadlineField = new DatePicker("Selection deadline");


        eventDataLayout.add(eventNameField);
        eventDataLayout.add(descriptionField);
        eventDataLayout.add(startDateField, endDateField);
        eventDataLayout.add(selectionDeadlineField);
        eventDataLayout.getElement().appendChild(ElementFactory.createBr());
        this.eventBinder = new Binder<>(Event.class);
        eventBinder.forField(eventNameField).asRequired().bind(Event::getName, Event::setName);
        eventBinder.forField(descriptionField).bind(Event::getDescription, Event::setDescription);
        eventBinder.forField(startDateField)
                .withValidator(start -> {
                    LocalDate end = endDateField.getValue();
                    if (start == null || end == null) {
                        return true;
                    }
                    return end.isBefore(start) ? false : true;
                }, "Start date must be after end date")
                .bind(Event::getFromDate, Event::setFromDate);
        eventBinder.forField(endDateField)
                .withValidator(end -> {
                    LocalDate start = startDateField.getValue();
                    if (start == null || end == null) {
                        return true;
                    }
                    return end.isBefore(start) ? false : true;
                }, "Start date must be after end date")
                .bind(Event::getToDate, Event::setToDate);

        eventBinder.forField(selectionDeadlineField)
                .bind(Event::getDeadline, Event::setDeadline);

        return eventDataLayout;
    }

    private void datesUpdated() {
        LocalDate startDate = startDateField.getValue();
        LocalDate endDate = endDateField.getValue();
        if (startDate == null || endDate == null) {
            return;
        }
        if (endDate.isBefore(startDate)) {
            return;
        }
        List<LocalDate> eventDays = startDate.datesUntil(endDate.plusDays(1))
                .collect(Collectors.toList());
        // TODO: use created event
        updateEventDays(eventDays);
    }

    private void updateEventDays(List<LocalDate> eventDays) {
        if (eventDays == null) {
            return;
        }
        eventDaysLayout.removeAll();
        for (int i = 0; i < eventDays.size(); i++) {
            LocalDate eventDate = eventDays.get(i);
            Details eventDayDetails = new Details();
            int dayNumber = i + 1;
            eventDayDetails.setSummary(new Span("Day " + dayNumber + " - " + eventDate.toString()));
            eventDayDetails.setContent(getEventDayContent(eventDate));
            eventDayDetails.setOpened(true);
            eventDaysLayout.add(eventDayDetails);
        }
    }

    private Component getEventDayContent(LocalDate eventDate) {
        VerticalLayout content = new VerticalLayout();

        List<EventMealDate> mealDates = mealDatesByEvent.get(eventDate);
        if ( mealDates == null || mealDates.isEmpty() ) {
            content.add(new Span("No existing menus yet for " + eventDate.toString()));
        } else {
            for (EventMealDate emd : mealDates) {
                Menu menu = emd.getMenu();
                HorizontalLayout holder = new HorizontalLayout();
                Span span = new Span(menu.getType().toString() + ": " + menu.getRestaurant());
                holder.add(span, new Button(VaadinIcon.MINUS.create(), e -> {
                    mealDatesByEvent.get(eventDate).remove(emd);
                    datesUpdated();
                }));
                holder.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                content.add(holder);
            }
        }

        content.add(new Button("Choose menu for date", e -> {
            Dialog dialog = new Dialog();
            Select<String> restaurantSelect = new Select<>();
            RadioButtonGroup<MealType> menuTypeRBG = new RadioButtonGroup<>();
            menuTypeRBG.setItems(MealType.values());
            menuTypeRBG.addValueChangeListener(v -> {
                switch (v.getValue()) {
                    case LUNCH -> {
                        restaurantSelect.setItems("Pizzeria Via Tribunali", "Thai in Town", "Taco Nito", "Kotipizza");
                        break;
                    }
                    case BREAKFAST -> {
                        restaurantSelect.setItems("Sodexo");
                        break;
                    }
                    case DESSERT -> {
                        restaurantSelect.setItems("Unica", "K-Citymarket Kupittaa");
                    }
                }

            });
            menuTypeRBG.setValue(MealType.LUNCH);

            Button confirmButton = new Button("Confirm");
            confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            Button cancelButton = new Button("Cancel");
            cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            confirmButton.addClickListener( confirmEvent -> {
                String restaurant = restaurantSelect.getValue();
                Menu selectedMenu = new Menu();
                selectedMenu.setRestaurant(restaurant);
                selectedMenu.setType(menuTypeRBG.getValue());
                EventMealDate eventMealDate = new EventMealDate();
                eventMealDate.setDate(eventDate);
                eventMealDate.setMenu(selectedMenu);
                List<EventMealDate> mealDates1 = mealDatesByEvent.get(eventDate);
                if (mealDates1 == null) {
                    mealDates1 = new ArrayList<>();
                    mealDatesByEvent.put(eventDate, mealDates1);
                }
                mealDates1.add(eventMealDate);
                dialog.close();
                datesUpdated();
            });
            cancelButton.addClickListener(cancelEv -> {
                dialog.close();
            });
            HorizontalLayout dialogButtons = new HorizontalLayout(confirmButton, cancelButton);
            VerticalLayout dialogContent = new VerticalLayout(menuTypeRBG, restaurantSelect, dialogButtons);
            dialog.add(dialogContent);
            dialog.open();


        }));
        return content;
    }


    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long eventId) {
        if (eventId == null) {
            event = new Event();
            return;
        }
        editEvent(eventId);
    }

    private void editEvent(Long eventId) {
        Optional<Event> eventById = eventService.getEventById(eventId);
        if (eventById.isPresent()) {
            event = eventById.get();
            eventBinder.readBean(event);
        } else {
            Notification.show("Unknown event id " + eventId);
            event = new Event();
        }

    }
}
