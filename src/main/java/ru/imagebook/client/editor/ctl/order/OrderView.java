package ru.imagebook.client.editor.ctl.order;

import java.util.List;
import java.util.Map;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Product;

public interface OrderView {
	void showOrderForm(Map<Integer, List<Product>> products, String locale, List<Order<?>> orders);

	void hideCreateOrderForm();

	void hideOpenOrderForm();

	void showOpenOrderForm(List<Order<?>> orders, String locale);

	void infoOrderProcessed();

	void showEditorCommonMessage(String errorMessage);
}
