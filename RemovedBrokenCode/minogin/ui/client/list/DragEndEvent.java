package ru.minogin.ui.client.list;

import com.google.gwt.event.shared.GwtEvent;

public class DragEndEvent<T> extends GwtEvent<DragEndHandler<T>> {
	public static final Type<DragEndHandler<?>> TYPE = new Type<DragEndHandler<?>>();

	private final T value;

	public DragEndEvent(T value) {
		this.value = value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Type<DragEndHandler<T>> getAssociatedType() {
		return (Type) TYPE;
	}

	@Override
	protected void dispatch(DragEndHandler<T> handler) {
		handler.onDragEnd(this);
	}

	public T getValue() {
		return value;
	}
}
