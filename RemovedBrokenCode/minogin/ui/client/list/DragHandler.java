package ru.minogin.ui.client.list;

import com.google.gwt.event.shared.EventHandler;

public interface DragHandler<T> extends EventHandler {
	void onDrag(DragEvent<T> event);
}
