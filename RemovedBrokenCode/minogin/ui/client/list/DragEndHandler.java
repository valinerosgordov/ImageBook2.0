package ru.minogin.ui.client.list;

import com.google.gwt.event.shared.EventHandler;

public interface DragEndHandler<T> extends EventHandler {
	void onDragEnd(DragEndEvent<T> event);
}
