package ru.minogin.ui.client.activetree;

import com.google.gwt.event.shared.EventHandler;

public interface SelectHandler<T> extends EventHandler {
	void onSelect(SelectEvent<T> event);
}
