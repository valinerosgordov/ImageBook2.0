package ru.minogin.ui.client.list;

import com.google.gwt.event.shared.GwtEvent;

public class ClearEvent extends GwtEvent<ClearHandler> {
	public static final Type<ClearHandler> TYPE = new Type<ClearHandler>();

	@Override
	public Type<ClearHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ClearHandler handler) {
		handler.onClear(this);
	}
}
