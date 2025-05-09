package ru.imagebook.client.editor.ctl.pages;

import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.editor.Layout;
import ru.imagebook.shared.model.editor.Page;

public interface PagesView {
	void showOrder(Order<?> order);

	void showPage(AlbumOrder order, int pageNumber, String sessionId);

	void selectPage(int pageNumber);

	void hidePages();

	void showMenu(Order<?> order, int currentVariant);

	void hideMenu();

	void hidePageCountWindow();

	void showPageCountWindow(Order<?> order);

	void showPageCountWarning();

	void activateConvertToIndividualPageButton(boolean activate);

	void showConvertingMessage(Integer type, Page page, String path, int pageNumber, boolean isShowMessage);
}
