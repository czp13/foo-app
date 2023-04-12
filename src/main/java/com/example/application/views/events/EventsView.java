package com.example.application.views.events;

import com.example.application.data.entity.Event;
import com.example.application.data.service.EventService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@PageTitle("Events")
@Route(value = "events", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class EventsView extends VerticalLayout {

    private EventService eventService;
    private List<Event> events;

    public EventsView(EventService eventService) {
        this.eventService = eventService;
        events = eventService.getEvents();
        showEvents(events);
        add(new Button("Create new event", e -> {
            UI.getCurrent().navigate(CreateEventView.class);
        }));
    }

    private void showEvents(List<Event> events) {
        if (events == null) {
            return;
        }
        for (Event event : events) {
            HorizontalLayout container = new HorizontalLayout();
            container.getStyle().set("border", "1px solid black");
            container.getStyle().set("border-radius", "4px");
            VerticalLayout eventInfoLayout = new VerticalLayout();
            eventInfoLayout.setMinWidth(480, Unit.PIXELS);
            eventInfoLayout.add(new Span("Event name: " + event.getName()));
            eventInfoLayout.add(new Span("Description " + event.getDescription()));
            eventInfoLayout.add(new Span(event.getFromDate().toString() + " - " + event.getToDate()));
            // eventInfoLayout.add(new Span("Participants - " + event.getParticipants().size()));
            eventInfoLayout.add(new Span("Selection deadline - " + (event.getDeadline() == null ? "" : event.getDeadline().toString())));

            VerticalLayout buttonsLayout = new VerticalLayout();

            Button editButton = new Button("Edit", e -> {
                UI.getCurrent().navigate(CreateEventView.class, event.getId());
            });
            buttonsLayout.add(editButton);
            container.add(eventInfoLayout);
            container.add(buttonsLayout);
            add(container);
        }

    }
}
