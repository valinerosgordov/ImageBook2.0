package ru.minogin.core.server.test;

public class AsyncResult<T> {
	private int delayMs;
	private int maxTimeMs;

	public AsyncResult() {
		this(50, 3000);
	}

	public AsyncResult(int delayMs, int maxTimeMs) {
		this.delayMs = delayMs;
		this.maxTimeMs = maxTimeMs;
	}

	public void waitFor(Value<T> value, T t) {
		int n = maxTimeMs / delayMs;
		for (int i = 0; i < n; i++) {
			if (value.get().equals(t))
				return;
			try {
				Thread.sleep(delayMs);
			}
			catch (InterruptedException e) {
				Thread.interrupted();
			}
		}

		throw new TestException();
	}
}
