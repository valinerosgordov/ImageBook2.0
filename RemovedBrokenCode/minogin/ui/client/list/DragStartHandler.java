package ru.minogin.ui.client.list;

import com.google.gwt.event.shared.EventHandler;

public interface DragStartHandler<T> extends EventHandler {
	void onDragStart(DragStartEvent<T> event);
}
