package ru.minogin.ui.client.list;

import com.google.gwt.event.shared.GwtEvent;

public class MoveEvent<T> extends GwtEvent<MoveHandler<T>> {
	public static final Type<MoveHandler<?>> TYPE = new Type<MoveHandler<?>>();

	private final T value;
	private final int targetIndex;

	public MoveEvent(T value, int targetIndex) {
		this.value = value;
		this.targetIndex = targetIndex;
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

	public int getTargetIndex() {
		return targetIndex;
	}
}
