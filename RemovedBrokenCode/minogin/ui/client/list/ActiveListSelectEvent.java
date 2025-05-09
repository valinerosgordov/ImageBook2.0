package ru.minogin.ui.client.list;

import com.google.gwt.event.shared.GwtEvent;

public class ActiveListSelectEvent<T> extends GwtEvent<ActiveListSelectHandler<T>> {
	public static final Type<ActiveListSelectHandler<?>> TYPE = new Type<ActiveListSelectHandler<?>>();

	private final T value;

	public ActiveListSelectEvent(T value) {
		this.value = value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Type<ActiveListSelectHandler<T>> getAssociatedType() {
		return (Type) TYPE;
	}

	@Override
	protected void dispatch(ActiveListSelectHandler<T> handler) {
		handler.onSelect(this);
	}

	public T getValue() {
		return value;
	}
}
