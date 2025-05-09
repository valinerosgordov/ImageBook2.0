package ru.minogin.core.client.bean;

import java.util.ArrayList;
import java.util.Collection;

public class ObservableSupport {
	private final ObservableBean bean;

	public ObservableSupport(ObservableBean bean) {
		this.bean = bean;
	}

	private final Collection<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.add(listener);
	}

	public void notifyPropertyChange(String name, Object oldValue, Object value) {
		PropertyChangeEvent event = new PropertyChangeEvent(bean, name, oldValue, value);

		for (PropertyChangeListener listener : listeners) {
			listener.onPropertyChange(event);

			if (event.getModifiedValue() != null)
				bean.directSet(name, event.getModifiedValue());
		}
	}
}
