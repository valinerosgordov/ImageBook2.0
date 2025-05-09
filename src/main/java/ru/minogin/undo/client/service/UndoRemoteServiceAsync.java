package ru.minogin.undo.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.minogin.undo.shared.UndoInfo;

public interface UndoRemoteServiceAsync {
	void undo(AsyncCallback<UndoInfo> callback);

	void resetUndoStack(AsyncCallback<Void> callback);
}
