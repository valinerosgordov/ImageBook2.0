package ru.minogin.ui.client.list;

import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.shared.GwtEvent;

public class MouseOverItemEvent<T> extends GwtEvent<MouseOverItemHandler<T>> {
	public static final Type<MouseOverItemHandler<?>> TYPE = new Type<MouseOverItemHandler<?>>();

	private final T value;
	private final ListItem<T> item;
	private final MouseOverEvent event;

	public MouseOverItemEvent(T value, ListItem<T> item,
			MouseOverEvent event) {
		this.value = value;
		this.item = item;
		this.event = event;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Type<MouseOverItemHandler<T>> getAssociatedType() {
		return (Type) TYPE;
	}

	@Override
	protected void dispatch(MouseOverItemHandler<T> handler) {
		handler.onMouseOverItem(this);
	}

	public T getValue() {
		return value;
	}

	public ListItem<T> getItem() {
		return item;
	}

	public MouseOverEvent getEvent() {
		return event;
	}
}
