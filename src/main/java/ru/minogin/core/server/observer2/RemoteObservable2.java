package ru.minogin.core.server.observer2;

import java.util.Collection;
import java.util.concurrent.*;

import ru.minogin.core.client.observer.Event;
import ru.minogin.core.client.observer.XListener;
import ru.minogin.core.client.observer2.Observable2;

public class RemoteObservable2<BE extends Event> implements Observable2<BE> {
	private final ConcurrentMap<BE, Collection<XListener<BE>>> listeners = new ConcurrentHashMap<BE, Collection<XListener<BE>>>();

	@Override
	@SuppressWarnings("unchecked")
	public <E extends BE> void addListener(E event, XListener<E> listener) {
		Collection<XListener<BE>> eventListeners = listeners.get(event);
		if (eventListeners == null) {
			eventListeners = new CopyOnWriteArrayList<XListener<BE>>();
			Collection<XListener<BE>> currentListeners = listeners.putIfAbsent(event, eventListeners);
			if (currentListeners != null)
				eventListeners = currentListeners;
		}

		eventListeners.add((XListener<BE>) listener);
	}

	@Override
	public <E extends BE> void removeListener(E event, XListener<E> listener) {
		Collection<XListener<BE>> eventListeners = listeners.get(event);
		if (eventListeners != null)
			eventListeners.remove(listener);
	}

	@Override
	public <E extends BE> void fireEvent(E event) {
		Collection<XListener<BE>> eventListeners = listeners.get(event);
		if (eventListeners != null) {
			for (XListener<BE> listener : eventListeners) {
				listener.handleEvent(event);
				if (event.removeListener())
					eventListeners.remove(listener); // TODO incorrect?
			}
		}
	}
}
