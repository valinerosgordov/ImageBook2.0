package ru.minogin.core.client.observer2;

import java.util.*;

import ru.minogin.core.client.observer.Event;
import ru.minogin.core.client.observer.XListener;

public class LocalObservable2<BE extends Event> implements Observable2<BE> {
	private final Map<BE, Collection<XListener<BE>>> listeners = new HashMap<BE, Collection<XListener<BE>>>();

	@Override
	@SuppressWarnings("unchecked")
	public <E extends BE> void addListener(E event, XListener<E> listener) {
		Collection<XListener<BE>> eventListeners = listeners.get(event);
		if (eventListeners == null) {
			eventListeners = new ArrayList<XListener<BE>>();
			listeners.put(event, eventListeners);
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

	public Map<BE, Collection<XListener<BE>>> getListeners() {
		return listeners;
	}
}
