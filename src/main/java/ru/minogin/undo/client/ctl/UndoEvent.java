package ru.minogin.undo.client.ctl;

import com.google.gwt.event.shared.GwtEvent;
import ru.minogin.undo.shared.UndoInfo;

public class UndoEvent extends GwtEvent<UndoHandler> {
	public static final Type<UndoHandler> TYPE = new Type<UndoHandler>();

	private final UndoInfo info;

	public UndoEvent(UndoInfo info) {
		this.info = info;
	}

	@Override
	public Type<UndoHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(UndoHandler handler) {
		handler.onUndo(this);
	}

	public UndoInfo getInfo() {
		return info;
	}
}
