package ru.minogin.undo.server.service;

import ru.minogin.undo.server.action.UndoableAction;
import ru.minogin.undo.shared.UndoInfo;

public class UndoRecord {
	private UndoableAction action;
	private UndoInfo info;

	public UndoRecord(UndoableAction action, UndoInfo info) {
		this.action = action;
		this.info = info;
	}

	public UndoableAction getAction() {
		return action;
	}

	public UndoInfo getInfo() {
		return info;
	}
}
