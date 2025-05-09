package ru.minogin.ui.client.list;

import com.google.gwt.event.shared.EventHandler;

public interface MoveHandler<T> extends EventHandler {
	void onMove(MoveEvent<T> event);
}
