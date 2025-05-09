package ru.minogin.undo.client.ctl;

import com.google.gwt.event.shared.GwtEvent;

public class ResetUndoStackEvent extends GwtEvent<ResetUndoStackHandler> {
	public static final Type<ResetUndoStackHandler> TYPE = new Type<ResetUndoStackHandler>();

	@Override
	public Type<ResetUndoStackHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ResetUndoStackHandler handler) {
		handler.onReset();
	}
}
