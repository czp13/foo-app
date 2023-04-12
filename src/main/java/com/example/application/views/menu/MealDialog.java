package com.example.application.views.menu;

import com.example.application.data.entity.DietAttributes;
import com.example.application.data.entity.Meal;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Consumer;

public class MealDialog extends Dialog {

    private final VerticalLayout content = new VerticalLayout();

    private final TextField nameTextField = new TextField("Name");
    private final TextArea ingredientsTextArea = new TextArea("Ingredients");
    private final MultiSelectComboBox<DietAttributes> attributesMultiSelectComboBox = new MultiSelectComboBox<>("Attributes");
    private final Binder<Meal> binder = new Binder<>(Meal.class);


    @Setter
    public Consumer<Meal> saveCallback;

    public MealDialog() {
        setHeaderTitle("Create Meal");
        binder.setBean(new Meal());
        init();
    }

    public MealDialog(Meal meal) {
        setHeaderTitle("Update Meal");
        binder.setBean(new Meal());
        binder.readBean(meal);
        init();
    }


    private void init() {
        content.setPadding(false);
        content.setSpacing(false);
        setWidth(300, Unit.PIXELS);
        ingredientsTextArea.setWidth(100, Unit.PERCENTAGE);
        attributesMultiSelectComboBox.setWidth(100, Unit.PERCENTAGE);
        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.setDisableOnClick(true);
        saveButton.addClickListener(event -> {
            saveButton.setEnabled(true);
            BinderValidationStatus<Meal> validationStatus = binder.validate();
            if(!validationStatus.isOk()){
                return;
            }
            saveCallback.accept(binder.getBean());
        });
        Button cancelButton = new Button("Cancel", e -> close());
        getFooter().add(cancelButton);
        getFooter().add(saveButton);

        binder.forField(nameTextField).asRequired("Name is required").bind(Meal::getName, Meal::setName);
        binder.forField(ingredientsTextArea).bind(Meal::getIngredients, Meal::setIngredients);

        attributesMultiSelectComboBox.setItems(DietAttributes.values());
        attributesMultiSelectComboBox.setItemLabelGenerator(DietAttributes::getName);
        binder.forField(attributesMultiSelectComboBox).bind(
                meal -> {
                    if(CollectionUtils.isEmpty(meal.getDietAttributes())){
                        return new HashSet<>();
                    }
                    return new HashSet<>(meal.getDietAttributes());
                },
                (meal, dietAttributes) -> meal.setDietAttributes(new ArrayList<>(dietAttributes)));

        content.add(nameTextField);
        content.add(ingredientsTextArea);
        content.add(attributesMultiSelectComboBox);
        add(content);
    }
}
