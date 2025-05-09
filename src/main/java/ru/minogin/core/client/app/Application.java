package ru.minogin.core.client.app;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class Application {
	@Inject
	private ApplicationController applicationController;

	public void run() {
		applicationController.run();
	}
}
