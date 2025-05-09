package ru.minogin.undo.client.view;

import ru.minogin.undo.client.ctl.UndoPresenter;

public interface UndoView {
	void setPresenter(UndoPresenter presenter);

	void showUndoMessage(int count);

	void hideUndoPanel();

	void enableUndoButton();

	void disableUndoButton();
}
