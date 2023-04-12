package com.example.application.views.events;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Events")
@Route(value = "events", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class EventsView extends VerticalLayout {

    public EventsView() {
        add(new Span("TODO: list existing events"));
        add(new Button("Create new event", e -> {
            UI.getCurrent().navigate(CreateEventView.class);
        }));
    }
}
