package ru.minogin.core.client.observer2;

import ru.minogin.core.client.observer.Event;
import ru.minogin.core.client.observer.XListener;

public interface Observable2<BE extends Event> {
	public <E extends BE> void addListener(E event, XListener<E> listener);

	public <E extends BE> void removeListener(E event, XListener<E> listener);

	public <E extends BE> void fireEvent(E event);
}
