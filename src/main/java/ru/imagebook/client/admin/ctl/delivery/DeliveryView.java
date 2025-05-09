package ru.imagebook.client.admin.ctl.delivery;

import java.util.List;

import ru.imagebook.shared.model.Order;

public interface DeliveryView {
	void showSection(String sessionId);

	void focusAddField();

	void showDeliveryMode();

	void focusDeliveryField();

	void showAddMode();

	void showCodeForm(Order<?> order);

	void showOrders(List<Order<?>> orders, String locale);

	void showTypeOrders(Integer deliveryType, List<Order<?>> orders, String locale);

	void setPresenter(DeliveryPresenter presenter);

	void download(String url);
}
