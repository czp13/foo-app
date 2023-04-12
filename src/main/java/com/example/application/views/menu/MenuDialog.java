package com.example.application.views.menu;

import com.example.application.data.entity.Meal;
import com.example.application.data.entity.MealType;
import com.example.application.data.entity.Menu;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import lombok.Setter;

import java.util.function.Consumer;

public class MenuDialog extends Dialog {

    private final VerticalLayout content = new VerticalLayout();
    private final TextField nameTextField = new TextField("Name");
    private final TextField restaurantTextField = new TextField("Restaurant Name");
    private final RadioButtonGroup<MealType> mealTypeRadioButtonGroup = new RadioButtonGroup<>("Type");
    private Binder<Menu> binder = new Binder<>(Menu.class);
    @Setter
    private Consumer<Menu> saveCallback;
    public MenuDialog(){
        setHeaderTitle("Create Menu");
        binder.setBean(new Menu());
        init();
    }

    public MenuDialog(Menu updatedMenu){
        setHeaderTitle("Update Menu");
        binder.setBean(new Menu());
        binder.readBean(updatedMenu);
        init();
    }
    private void init(){
        setWidth(450, Unit.PIXELS);

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.setDisableOnClick(true);
        saveButton.addClickListener(event -> {
            saveButton.setEnabled(true);
            BinderValidationStatus<Menu> validationStatus = binder.validate();
            if(!validationStatus.isOk()){
                return;
            }
            saveCallback.accept(binder.getBean());
        });
        Button cancelButton = new Button("Cancel", e -> close());
        getFooter().add(cancelButton);
        getFooter().add(saveButton);

        nameTextField.setWidth(100, Unit.PERCENTAGE);
        restaurantTextField.setWidth(100, Unit.PERCENTAGE);
        mealTypeRadioButtonGroup.setWidth(100, Unit.PERCENTAGE);
        mealTypeRadioButtonGroup.setItemLabelGenerator(MealType::getName);
        mealTypeRadioButtonGroup.setItems(MealType.values());

        binder.forField(nameTextField).asRequired("Name is required").bind(Menu::getName, Menu::setName);
        binder.forField(restaurantTextField).bind(Menu::getRestaurant, Menu::setRestaurant);
        binder.forField(mealTypeRadioButtonGroup).bind(Menu::getType, Menu::setType);

        content.setPadding(false);
        content.add(nameTextField);
        content.add(restaurantTextField);
        content.add(mealTypeRadioButtonGroup);
        add(content);
    }
}
