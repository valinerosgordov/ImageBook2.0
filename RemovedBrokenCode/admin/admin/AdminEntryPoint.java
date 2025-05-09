package ru.imagebook.client.admin;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class AdminEntryPoint implements EntryPoint {
	private final AdminInjector injector = GWT.create(AdminInjector.class);

	@Override
	public void onModuleLoad() {
		Admin admin = injector.getAdmin();
		admin.start();
	}
}
