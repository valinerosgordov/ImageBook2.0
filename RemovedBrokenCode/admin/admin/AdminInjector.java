package ru.imagebook.client.admin;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules( { AdminModule.class })
public interface AdminInjector extends Ginjector {
	Admin getAdmin();
}
