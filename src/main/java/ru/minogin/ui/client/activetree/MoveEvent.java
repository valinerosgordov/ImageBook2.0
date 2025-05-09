package ru.minogin.ui.client.activetree;

import com.google.gwt.event.shared.GwtEvent;

public class MoveEvent<T> extends GwtEvent<MoveHandler<T>> {
	public static final Type<MoveHandler<?>> TYPE = new Type<MoveHandler<?>>();

	private final T value;
	private final T parent;
	private final int index;

	public MoveEvent(T value, T parent, int index) {
		this.value = value;
		this.parent = parent;
		this.index = index;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Type<MoveHandler<T>> getAssociatedType() {
		return (Type) TYPE;
	}

	@Override
	protected void dispatch(MoveHandler<T> handler) {
		handler.onMove(this);
	}

	public T getValue() {
		return value;
	}
	
	public T getParent() {
		return parent;
	}

	public int getIndex() {
		return index;
	}
}
