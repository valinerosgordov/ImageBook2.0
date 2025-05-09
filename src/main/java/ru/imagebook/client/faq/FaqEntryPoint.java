package ru.imagebook.client.faq;

import ru.minogin.core.client.app.ApplicationEntryPoint;
import ru.minogin.core.client.app.ApplicationInjector;

import com.google.gwt.core.client.GWT;

public class FaqEntryPoint extends ApplicationEntryPoint {

	public FaqEntryPoint() {
		super((ApplicationInjector) GWT.create(FaqInjector.class));
	}
 }
