package ru.minogin.ui.client.activetree;

import com.google.gwt.event.shared.GwtEvent;

public class SelectEvent<T> extends GwtEvent<SelectHandler<T>> {
	public static final Type<SelectHandler<?>> TYPE = new Type<SelectHandler<?>>();

	private final T value;

	public SelectEvent(T value) {
		this.value = value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Type<SelectHandler<T>> getAssociatedType() {
		return (Type) TYPE;
	}

	@Override
	protected void dispatch(SelectHandler<T> handler) {
		handler.onSelect(this);
	}

	public T getValue() {
		return value;
	}
}
