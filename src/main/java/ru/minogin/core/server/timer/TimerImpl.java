package ru.minogin.core.server.timer;

import java.util.TimerTask;

import ru.minogin.core.client.timer.Timer;

public class TimerImpl implements Timer {
	private final java.util.Timer timer;
	private final Runnable runnable;

	public TimerImpl(Runnable runnable) {
		this.runnable = runnable;
		timer = new java.util.Timer();
	}

	@Override
	public void scheduleRepeating(int periodMillis) {
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				runnable.run();
			}
		}, periodMillis, periodMillis);
	}

	@Override
	public void cancel() {
		if (timer != null)
			timer.cancel();
	}
}
