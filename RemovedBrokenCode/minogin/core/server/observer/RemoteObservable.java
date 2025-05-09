package ru.minogin.core.server.observer;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import ru.minogin.core.client.observer.*;

public class RemoteObservable<BE extends Event> implements Observable<BE> {
	private final ConcurrentMap<String, Collection<XListener<BE>>> listeners = new ConcurrentHashMap<String, Collection<XListener<BE>>>();

	@Override
	@SuppressWarnings("unchecked")
	public <E extends BE> void addListener(String eventType, XListener<E> listener) {
		Collection<XListener<BE>> eventListeners = listeners.get(eventType);
		if (eventListeners == null) {
			eventListeners = new CopyOnWriteArrayList<XListener<BE>>(); 
			Collection<XListener<BE>> currentListeners = listeners.putIfAbsent(eventType, eventListeners);
			if (currentListeners != null)
				eventListeners = currentListeners;
		}

		eventListeners.add((XListener<BE>) listener);
	}

	@Override
	public <E extends BE> void removeListener(String eventType, XListener<E> listener) {
		Collection<XListener<BE>> eventListeners = listeners.get(eventType);
		if (eventListeners != null)
			eventListeners.remove(listener);
	}

	@Override
	public <E extends BE> void fireEvent(E event) {
		Collection<XListener<BE>> eventListeners = listeners.get(event.getType());
		if (eventListeners != null) {
			for (XListener<BE> listener : eventListeners) {
				listener.handleEvent(event);
				if (event.removeListener())
					eventListeners.remove(listener); // TODO incorrect?
			}
		}
	}
}
