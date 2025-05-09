package ru.minogin.core.server.sandbox;

import java.lang.Thread.UncaughtExceptionHandler;

import ru.minogin.core.client.exception.Exceptions;

/**
 * @author Andrey Minogin
 *
 * Frame value is a measure of latency. It is average response time of "runnable".
 * It cannot be too small because there will appear overcome of thread operations.
 *
 */

// TODO known problems
// 1. deprecated methods are not safe
// 2. every run will last at least frame * cpuUsage even if computations take less time
// 3. best FRAME value?
// 4. Can we use thread pool somehow?

public class SandboxRunner<V> {
	private static final long MS = 1000 * 1000;
	private static final long FRAME = 100 * MS;

	private double cpuUsage;
	private long timeoutMS;
	private Throwable exception;
	private V result;

	public SandboxRunner(double cpuUsage, long timeoutMS) {
		this.cpuUsage = cpuUsage;
		this.timeoutMS = timeoutMS;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public V run(final XCallable<V> callable) {
		long active = (long) (cpuUsage * FRAME);
		long sleep = FRAME - active;

		long activeMillis = active / MS;
		int activeNanos = (int) (active % MS);

		long sleepMillis = sleep / MS;
		int sleepNanos = (int) (sleep % MS);

		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				result = callable.call();
			}
		};

		Thread target = new Thread(runnable);
		target.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				exception = e;
			}
		});
		long stopTime = System.currentTimeMillis() + timeoutMS;
		target.start();

		try {
			while (true) {
				Thread.sleep(activeMillis, activeNanos);

				if (!target.isAlive()) {
					if (exception != null) {
						return (V) Exceptions.rethrow(exception);
					}
					return result;
				}

				if (timeoutMS != 0 && System.currentTimeMillis() > stopTime) {
					target.stop();
					break;
				}

				target.suspend();
				Thread.sleep(sleepMillis, sleepNanos);
				target.resume();
			}
		}
		catch (InterruptedException e) {
			Thread.interrupted();
		}

		return null;
	}
}
