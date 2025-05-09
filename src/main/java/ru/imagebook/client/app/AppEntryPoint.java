package ru.imagebook.client.app;

import com.google.gwt.core.client.EntryPoint;


public class AppEntryPoint implements EntryPoint {
    @Override
    public void onModuleLoad() {
        AppInjector.INSTANCE.getAppController()
            .start();
    }
}