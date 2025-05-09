package ru.minogin.undo.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import ru.minogin.undo.shared.UndoInfo;

@RemoteServiceRelativePath("undo.remoteService")
public interface UndoRemoteService extends RemoteService {
	UndoInfo undo();

	void resetUndoStack();
}
