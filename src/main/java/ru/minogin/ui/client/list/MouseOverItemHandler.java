package ru.minogin.ui.client.list;

import com.google.gwt.event.shared.EventHandler;

public interface MouseOverItemHandler<T> extends EventHandler {
	void onMouseOverItem(MouseOverItemEvent<T> event);
}
