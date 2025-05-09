package ru.minogin.core.server.util;

public class Blocker {
	public synchronized void pause() {
		try {
			wait(1000);
		}
		catch (InterruptedException e) {
		}
	}

	public synchronized void resume() {
		notifyAll();
	}
}
