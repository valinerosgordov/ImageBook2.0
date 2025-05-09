package ru.minogin.undo.server.action;

public interface UndoableAction {
	void execute();

	void undo();
}
