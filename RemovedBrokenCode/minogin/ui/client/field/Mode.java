package ru.minogin.ui.client.field;

public enum Mode {
	/** Normal mode - show border on mouse over and edit on click (focus). */
	NORMAL,

	/** Read only mode - no border and editing. */
	READONLY,

	/** Field behaves as ordinary input - editing only. */
	INPUT;
}
