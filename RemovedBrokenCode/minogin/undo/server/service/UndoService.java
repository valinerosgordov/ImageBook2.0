package ru.minogin.undo.server.service;

import ru.minogin.undo.server.action.UndoableAction;
import ru.minogin.undo.shared.UndoInfo;

public interface UndoService {
	void execute(UndoableAction action);

	void execute(UndoableAction action, UndoInfo info);

	UndoInfo undo();
}
