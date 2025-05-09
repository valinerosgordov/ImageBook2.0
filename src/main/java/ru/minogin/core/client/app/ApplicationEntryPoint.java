package ru.minogin.core.client.app;

import com.google.gwt.core.client.EntryPoint;

public abstract class ApplicationEntryPoint implements EntryPoint {
	private final ApplicationInjector injector;

	public ApplicationEntryPoint(ApplicationInjector injector) {
		this.injector = injector;
	}

	@Override
	public void onModuleLoad() {
		Application application = injector.getApplication();
		application.run();
	}
}
