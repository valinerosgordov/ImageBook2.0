package ru.minogin.core.client.observer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LocalObservable<BE extends Event> implements Observable<BE> {
	private final Map<String, Collection<XListener<BE>>> listeners = new HashMap<String, Collection<XListener<BE>>>();

	@Override
	@SuppressWarnings("unchecked")
	public <E extends BE> void addListener(String eventType, XListener<E> listener) {
		Collection<XListener<BE>> eventListeners = listeners.get(eventType);
		if (eventListeners == null) {
			eventListeners = new ArrayList<XListener<BE>>();
			listeners.put(eventType, eventListeners);
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

	public Map<String, Collection<XListener<BE>>> getListeners() {
		return listeners;
	}
}
