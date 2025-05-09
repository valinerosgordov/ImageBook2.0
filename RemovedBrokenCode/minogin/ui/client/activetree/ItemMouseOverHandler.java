package ru.minogin.ui.client.activetree;

import com.google.gwt.event.shared.EventHandler;

public interface ItemMouseOverHandler<T> extends EventHandler {
	void onItemMouseOver(ItemMouseOverEvent<T> event);
}
