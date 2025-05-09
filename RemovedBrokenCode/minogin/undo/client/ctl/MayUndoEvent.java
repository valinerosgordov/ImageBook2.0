package ru.minogin.undo.client.ctl;

import com.google.gwt.event.shared.GwtEvent;

public class MayUndoEvent extends GwtEvent<MayUndoHandler> {
	public static final Type<MayUndoHandler> TYPE = new Type<MayUndoHandler>();

	@Override
	public Type<MayUndoHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MayUndoHandler handler) {
		handler.onMayUndo();
	}
}
