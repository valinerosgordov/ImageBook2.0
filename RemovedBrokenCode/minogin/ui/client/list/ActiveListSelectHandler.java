package ru.minogin.ui.client.list;

import com.google.gwt.event.shared.EventHandler;

public interface ActiveListSelectHandler<T> extends EventHandler {
	void onSelect(ActiveListSelectEvent<T> event);
}
