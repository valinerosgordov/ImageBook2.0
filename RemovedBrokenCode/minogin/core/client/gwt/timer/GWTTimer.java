package ru.minogin.core.client.gwt.timer;

import ru.minogin.core.client.timer.Timer;

public class GWTTimer implements Timer {
	private final com.google.gwt.user.client.Timer timer;

	public GWTTimer(final Runnable runnable) {
		timer = new com.google.gwt.user.client.Timer() {
			@Override
			public void run() {
				runnable.run();
			}
		};
	}

	@Override
	public void scheduleRepeating(int periodMillis) {
		timer.scheduleRepeating(periodMillis);
	}

	@Override
	public void cancel() {
		timer.cancel();
	}
}
