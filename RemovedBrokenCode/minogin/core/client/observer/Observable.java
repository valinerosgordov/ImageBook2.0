package ru.minogin.core.client.observer;

public interface Observable<BE extends Event> {
	public <E extends BE> void addListener(String eventType, XListener<E> listener);

	public <E extends BE> void removeListener(String eventType, XListener<E> listener);

	public <E extends BE> void fireEvent(E event);
}
