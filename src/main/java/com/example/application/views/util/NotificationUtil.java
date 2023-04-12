package com.example.application.views.util;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class NotificationUtil {

    private NotificationUtil(){

    }
    public static void showSuccess(String text){
        Notification notification = new Notification(text);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setDuration(3000);
        notification.open();
    }
}
