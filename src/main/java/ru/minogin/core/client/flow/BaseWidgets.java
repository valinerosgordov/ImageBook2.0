package ru.minogin.core.client.flow;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;

@Singleton
public class BaseWidgets implements Widgets {
	private Map<String, Object> widgets = new HashMap<String, Object>();

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String id) {
		Object widget = widgets.get(id);
		return (T) widget;
	}

	@Override
	public void add(String id, Object widget) {
		widgets.put(id, widget);
	}

	@Override
	public void remove(String id) {
		widgets.remove(id);
	}
}
