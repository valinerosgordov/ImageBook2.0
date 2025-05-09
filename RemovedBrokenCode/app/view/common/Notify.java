package ru.imagebook.client.app.view.common;

import org.gwtbootstrap3.extras.bootbox.client.Bootbox;

import com.google.gwt.core.client.GWT;

import ru.minogin.core.client.constants.CommonConstants;


public class Notify {
    private static CommonConstants commonConstants = GWT.create(CommonConstants.class);

    private Notify() {
    }

    public static void info(String message) {
        notify(commonConstants.info(), message);
    }

    public static void error(String message) {
        notify(commonConstants.error(), message);
    }

    public static void notify(String title, String message) {
        Bootbox.Dialog.create()
            .setTitle(title)
            .setMessage(message)
            .addButton(commonConstants.ok())
            .setCloseButton(true)
            .show();
    }
}
