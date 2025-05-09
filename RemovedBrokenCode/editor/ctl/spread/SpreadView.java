package ru.imagebook.client.editor.ctl.spread;

import ru.imagebook.shared.model.Order;

public interface SpreadView {
	void showSpread(Order<?> order, int pageNumber, String sessionId);

	void hideSpread();
}
