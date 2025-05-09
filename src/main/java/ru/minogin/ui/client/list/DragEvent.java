package ru.minogin.ui.client.list;

import com.google.gwt.event.shared.GwtEvent;

public class DragEvent<T> extends GwtEvent<DragHandler<T>> {
	public static final Type<DragHandler<?>> TYPE = new Type<DragHandler<?>>();

	private final T value;

	public DragEvent(T value) {
		this.value = value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Type<DragHandler<T>> getAssociatedType() {
		return (Type) TYPE;
	}

	@Override
	protected void dispatch(DragHandler<T> handler) {
		handler.onDrag(this);
	}

	public T getValue() {
		return value;
	}
}
