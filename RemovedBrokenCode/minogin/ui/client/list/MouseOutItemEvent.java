package ru.minogin.ui.client.list;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.shared.GwtEvent;

public class MouseOutItemEvent<T> extends GwtEvent<MouseOutItemHandler<T>> {
	public static final Type<MouseOutItemHandler<?>> TYPE = new Type<MouseOutItemHandler<?>>();

	private final T value;
	private final ListItem<T> item;
	private final MouseOutEvent event;

	public MouseOutItemEvent(T value, ListItem<T> item, MouseOutEvent event) {
		this.value = value;
		this.item = item;
		this.event = event;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Type<MouseOutItemHandler<T>> getAssociatedType() {
		return (Type) TYPE;
	}

	@Override
	protected void dispatch(MouseOutItemHandler<T> handler) {
		handler.onMouseOutItem(this);
	}

	public T getValue() {
		return value;
	}

	public ListItem<T> getItem() {
		return item;
	}

	public MouseOutEvent getEvent() {
		return event;
	}
}
