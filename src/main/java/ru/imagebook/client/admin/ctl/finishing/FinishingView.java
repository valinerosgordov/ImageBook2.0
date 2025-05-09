package ru.imagebook.client.admin.ctl.finishing;

import java.util.List;

import ru.imagebook.shared.model.Order;

public interface FinishingView {
	void showSection();

	void showOrders(List<Order<?>> orders, String locale);

	void resetOrder();

	void focusNumberField();

	void resetNumberField();

	void showOrder(Order<?> order, String locale, String sessionId);
}
