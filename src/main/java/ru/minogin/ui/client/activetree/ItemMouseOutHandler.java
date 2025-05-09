package ru.minogin.ui.client.activetree;

import com.google.gwt.event.shared.EventHandler;

public interface ItemMouseOutHandler<T> extends EventHandler {
	void onItemMouseOut(ItemMouseOutEvent<T> event);
}
