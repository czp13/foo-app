package com.example.application.views.menu;

import com.example.application.data.entity.Menu;
import com.example.application.service.MenuService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.Optional;

@PageTitle("Menu")
@Route(value = "menu", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class MenuView extends VerticalLayout {

    private final Grid<Menu> menuGrid = new Grid<>();
    private final MenuService menuService;
    private final Button addBtn = new Button("Add", VaadinIcon.PLUS.create());
    private final Button updateBtn = new Button("Update");
    private final Button deleteBtn = new Button("Delete", VaadinIcon.MINUS.create());
    public MenuView(MenuService menuService){
        this.menuService = menuService;
        menuGrid.setItems(this.menuService.findAll());
        menuGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        menuGrid.addColumn(Menu::getName).setHeader("Name");
        menuGrid.addColumn(Menu::getRestaurant).setHeader("Restaurant");
        menuGrid.addColumn(menu -> {
            if(menu.getType() == null){
                return "";
            }
            return menu.getType().getName();
        }).setHeader("Type");


        addBtn.addClickListener(event -> {
           MenuDialog menuDialog = new MenuDialog();
           menuDialog.setSaveCallback(savedMenu -> {
               this.menuService.save(savedMenu);
               menuGrid.setItems(this.menuService.findAll());
               menuDialog.close();
           });
           menuDialog.open();
        });
        deleteBtn.addClickListener(event -> {
            Optional<Menu> optionalMenu = menuGrid.asSingleSelect().getOptionalValue();
            if(optionalMenu.isPresent()){
                ConfirmDialog confirmDialog = new ConfirmDialog("Sure?",
                        "Do you want to delete "+optionalMenu.get().getName() + " ?", "Yes",
                        yesEvent -> {
                                menuService.deleteById(optionalMenu.get().getId());
                                menuGrid.setItems(menuService.findAll());
                                Notification notification = new Notification("Menu successfully deleted");
                                notification.setDuration(3000);
                                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                                notification.open();
                                updateBtn.setEnabled(false);
                                deleteBtn.setEnabled(false);
                                yesEvent.getSource().close();
                        }, "No", noEvent -> noEvent.getSource().close());
                confirmDialog.open();
            }
        });


        menuGrid.addSelectionListener(event -> {
            Optional<Menu> firstSelectedItem = event.getFirstSelectedItem();
            if(firstSelectedItem.isPresent()){
                updateBtn.setEnabled(true);
                deleteBtn.setEnabled(true);
            }
        });

        addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
        updateBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        HorizontalLayout buttonLayout = new HorizontalLayout(deleteBtn, updateBtn, addBtn);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.setWidth(100, Unit.PERCENTAGE);
        add(menuGrid, buttonLayout);
    }


}
