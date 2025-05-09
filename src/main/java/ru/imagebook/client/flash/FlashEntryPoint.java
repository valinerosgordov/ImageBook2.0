package ru.imagebook.client.flash;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class FlashEntryPoint implements EntryPoint {
	@Override
	public void onModuleLoad() {
		FlashInjector injector = GWT.create(FlashInjector.class);
		FlashApp flashApp = injector.getFlashApp();
		flashApp.start();
	}
}
