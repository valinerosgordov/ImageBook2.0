package ru.minogin.ui.client.list;

import com.google.gwt.event.shared.EventHandler;

public interface MouseOutItemHandler<T> extends EventHandler {
	void onMouseOutItem(MouseOutItemEvent<T> event);
}
