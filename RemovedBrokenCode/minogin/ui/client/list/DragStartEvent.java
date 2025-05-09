package ru.minogin.ui.client.list;

import com.google.gwt.event.shared.GwtEvent;

public class DragStartEvent<T> extends GwtEvent<DragStartHandler<T>> {
	public static final Type<DragStartHandler<?>> TYPE = new Type<DragStartHandler<?>>();

	private final T value;

	public DragStartEvent(T value) {
		this.value = value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Type<DragStartHandler<T>> getAssociatedType() {
		return (Type) TYPE;
	}

	@Override
	protected void dispatch(DragStartHandler<T> handler) {
		handler.onDragStart(this);
	}

	public T getValue() {
		return value;
	}
}
