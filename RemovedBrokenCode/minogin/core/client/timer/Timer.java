package ru.minogin.core.client.timer;

public interface Timer {
	void scheduleRepeating(int periodMillis);

	void cancel();
}
