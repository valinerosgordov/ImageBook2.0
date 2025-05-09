package ru.minogin.core.client.ui;

import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.timer.Timer;

public class ElementWaiter {
	private static final int PERIOD_MS = 200;
	private static final int TIMEOUT_MS = 30000;

	private final CoreFactory factory;
	private final int periodMS;
	private final int timeOutMS;
	private Timer timer;

	public ElementWaiter(CoreFactory factory) {
		this(factory, PERIOD_MS, TIMEOUT_MS);
	}

	public ElementWaiter(CoreFactory factory, int periodMS, int timeOutMS) {
		this.factory = factory;
		this.periodMS = periodMS;
		this.timeOutMS = timeOutMS;
	}

	public void waitFor(final String elementId, final WaiterCallback callback) {
		if (XDOM.exists(elementId))
			callback.onSuccess();
		else {
			final long startTime = System.currentTimeMillis();
			timer = factory.createTimer(new Runnable() {
				@Override
				public void run() {
					if (XDOM.exists(elementId)) {
						timer.cancel();

						callback.onSuccess();
					}
					else {
						if (System.currentTimeMillis() - startTime > timeOutMS) {
							timer.cancel();

							callback.onTimeout();
						}
					}
				}
			});
			timer.scheduleRepeating(periodMS);
		}
	}
}
