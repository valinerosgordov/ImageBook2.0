package ru.imagebook.client.app;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

import ru.imagebook.client.app.ctl.AppController;


@GinModules({ AppModule.class} )
public interface AppInjector extends Ginjector {
    AppInjector INSTANCE = GWT.create(AppInjector.class);

    AppController getAppController();
}
