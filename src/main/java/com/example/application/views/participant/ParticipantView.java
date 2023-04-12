package com.example.application.views.participant;

import com.example.application.data.dto.UserChoiceDto;
import com.example.application.data.entity.User;
import com.example.application.data.service.ParticipantService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.gridpro.GridProVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.Arrays;
import java.util.List;

@PageTitle("Food Choice")
@Route(value = "participant", layout = MainLayout.class)
@RolesAllowed("USER")
public class ParticipantView extends Div {

    private GridPro<UserChoiceDto> grid = new GridPro<>(UserChoiceDto.class);
    private Button saveButton = new Button("Save");

    public ParticipantView() {
        ParticipantService participantService = new ParticipantService();
        grid.setEditOnClick(true);
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
        grid.addEditColumn(user -> createStatusIcon(user.getConfirmed())).checkbox(UserChoiceDto::setConfirmed).setHeader("Confirmed");

        grid.addSelectionListener(event -> {
            if (event.getFirstSelectedItem().isPresent()) {
                saveButton.setEnabled(true);
            }
        });

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.setDisableOnClick(true);
        saveButton.setEnabled(false);

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonLayout.setWidth(100, Unit.PERCENTAGE);

        add(grid);
        add(buttonLayout);
    }

    private Icon createStatusIcon(Boolean confirmed) {
        Icon icon;

        if(confirmed == null) {
            return null;
        }

        if (confirmed) {
            icon = VaadinIcon.CHECK.create();
            icon.getElement().getThemeList().add("badge success");
        } else {
            icon = VaadinIcon.CLOSE_SMALL.create();
            icon.getElement().getThemeList().add("badge error");
        }
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        return icon;
    }
}
