package com.example.application.views.menu;

import com.example.application.data.entity.Meal;
import com.example.application.data.entity.Menu;
import com.example.application.service.MenuService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@PageTitle("Meal")
@Route(value = "meal", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class MealView extends VerticalLayout {

    private final Grid<Meal> mealGrid;
    private MealDialog mealDialog;
    private Menu selectedMenu;
    private List<Menu> menus;
    public MealView(MenuService menuService){
        menus = menuService.findAll();

        mealGrid = new Grid<>();
        mealGrid.setEnabled(false);
        mealGrid.addColumn(Meal::getName).setHeader("Name");
        mealGrid.addColumn(Meal::getIngredients).setHeader("Ingredients");
        mealGrid.addComponentColumn(meal -> {
            Div div = new Div();
            meal.getDietAttributes().forEach(dietAttribute -> {
                Span badge = new Span(dietAttribute.getName());
                badge.getElement().getThemeList().add("badge");
                div.add(badge);
            });
            return div;
        }).setHeader("Attributes").setKey("attributes");

        Button addBtn = new Button("Add", VaadinIcon.PLUS.create());
        addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button deleteBtn = new Button("Delete", VaadinIcon.MINUS.create());
        deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Button updateBtn = new Button("Update");
        addBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        updateBtn.setEnabled(false);
        addBtn.addClickListener(event -> {
           mealDialog = new MealDialog();
           mealDialog.setSaveCallback(meal -> {


               menuService.save(selectedMenu);
               //TODO save it to menu service
               mealDialog.close();
               mealDialog = null;
           });
           mealDialog.open();
        });
        HorizontalLayout buttonLayout = new HorizontalLayout(deleteBtn, updateBtn, addBtn);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.setWidth(100, Unit.PERCENTAGE);

        ComboBox<Menu> menuComboBox = new ComboBox<>("Select menu");
        menuComboBox.setItemLabelGenerator(Menu::getName);
        menuComboBox.setItems(menus);
        menuComboBox.addValueChangeListener(event -> {
            Menu value = event.getValue();
            if(value == null){
                return;
            }
            selectedMenu = value;
            mealGrid.setItems(selectedMenu.getMeals());
            addBtn.setEnabled(true);
        });

        add(menuComboBox);
        add(new Hr());
        add(new H2("Meals"));
        add(mealGrid);
        add(buttonLayout);
    }


}
