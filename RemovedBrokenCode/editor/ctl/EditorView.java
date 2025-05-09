package ru.imagebook.client.editor.ctl;

import ru.imagebook.shared.model.Vendor;

public interface EditorView {
	void layoutDesktop(Vendor vendor);

	void showDesktop();

	void enableProcessButton();

	void disableProcessButton();

	void enableCloseButton();

	void disableCloseButton();

	void alertIE();

	void enableDisposeButton();

	void disableDisposeButton();

	void showDisposeProgress();

	void hideDisposeProgress();

	void showBanner(String bannerText);
}
