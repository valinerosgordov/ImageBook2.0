package ru.minogin.core.server.util;

public class ThreadUtil {
	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		}
		catch (InterruptedException e) {
			Thread.interrupted();
		}
	}
}
