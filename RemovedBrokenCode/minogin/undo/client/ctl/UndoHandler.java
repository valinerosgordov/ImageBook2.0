package ru.minogin.undo.client.ctl;

import com.google.gwt.event.shared.EventHandler;

public interface UndoHandler extends EventHandler {
	void onUndo(UndoEvent event);
}
