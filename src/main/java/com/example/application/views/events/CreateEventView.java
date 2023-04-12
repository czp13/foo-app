package com.example.application.views.events;

import com.example.application.data.entity.Event;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Create Event")
@Route(value = "create-event", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class CreateEventView extends VerticalLayout {

    private DatePicker startDateField;
    private DatePicker endDateField;
    private TextField eventNameField;
    private Button saveChanges;
    private VerticalLayout eventDaysLayout;

    private Event event;
    private Binder<Event> eventBinder;
    private DatePicker selectionDeadlineField;

    public CreateEventView() {
        event = new Event();

        setMargin(true);

        HorizontalLayout header = new HorizontalLayout();
        header.setPadding(true);
        header.setWidth(100, Unit.PERCENTAGE);
        saveChanges = new Button("Save changes");
        saveChanges.addClickListener(e -> {
            try {
                eventBinder.writeBean(event);
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
        eventDataLayout.getElement().appendChild(ElementFactory.createBr());
        eventDataLayout.add(startDateField, endDateField);
        eventDataLayout.add(selectionDeadlineField);
        eventDataLayout.getElement().appendChild(ElementFactory.createBr());
        this.eventBinder = new Binder<>(Event.class);
        eventBinder.forField(eventNameField).asRequired().bind(Event::getName, Event::setName);
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
        updateEventDays(null, eventDays);
    }

    private void updateEventDays(Event event, List<LocalDate> eventDays) {
        if (eventDays == null) {
            return;
        }
        eventDaysLayout.removeAll();
        for (int i = 0; i < eventDays.size(); i++) {
            LocalDate eventDate = eventDays.get(i);
            // TODO: check for existing date in Event
            Details eventDayDetails = new Details();
            int dayNumber = i + 1;
            eventDayDetails.setSummary(new Span("Day " + dayNumber + " - " + eventDate.toString()));
            eventDayDetails.setContent(getEventDayContent(eventDate));
            eventDaysLayout.add(eventDayDetails);
        }
    }

    private Component getEventDayContent(LocalDate eventDate) {
        VerticalLayout content = new VerticalLayout();
        content.add(new Span("TODO: Dummy event day content for " + eventDate.toString()));
        content.add(new Button("Create new menu"));
        content.add(new Button("Choose existing menu"));
        return content;
    }


}
