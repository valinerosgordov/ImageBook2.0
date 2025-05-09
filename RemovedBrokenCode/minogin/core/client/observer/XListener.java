package ru.minogin.core.client.observer;

public interface XListener<E extends Event> {
	public void handleEvent(E event);
}
