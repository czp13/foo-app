package com.example.application.views.participant;

import com.example.application.data.dto.UserChoiceDto;
import com.example.application.data.entity.User;
import com.example.application.data.service.ParticipantService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.gridpro.GridProVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Food Choice")
@Route(value = "participant", layout = MainLayout.class)
@RolesAllowed("USER")
public class ParticipantView extends Div {

    private GridPro<UserChoiceDto> grid = new GridPro<>(UserChoiceDto.class);

    public ParticipantView() {
        ParticipantService participantService = new ParticipantService();
        grid.setEditOnClick(true);
//        grid.setWidthFull();
//        grid.setHeightFull();
        grid.addThemeVariants(GridProVariant.LUMO_HIGHLIGHT_EDITABLE_CELLS);

        grid.setItems(participantService.getChoices(new User()));
        grid.setColumns("eventDateTime", "deadlineDateTime", "eventName", "eventDescription", "menuName");
//        grid.addColumn(new ComponentRenderer<>(userChoiceDto -> {
//            String menuOptions = String.join(" ", userChoiceDto.getPotentialMealName());
//            return menuOptions;
//        })).setHeader("Menu Options");

        // Fix this to be dynamic:
        List<String> mealNames = Arrays.asList("Burger", "Pizza");
        grid.addEditColumn(UserChoiceDto::getPotentialMealName).select(UserChoiceDto::setChosenMealName, mealNames).setHeader("Chosen Meal");

        grid.addEditColumn(UserChoiceDto::getNotesForOrganizer).text(UserChoiceDto::setNotesForOrganizer).setHeader("Notes for Food");
        grid.addEditColumn(UserChoiceDto::getConfirmed).checkbox(UserChoiceDto::setConfirmed).setHeader("Confirmed");

        add(grid);
    }
}
