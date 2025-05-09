package ru.imagebook.client.admin.ctl.request;

import java.util.List;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Request;

public interface RequestView {
	void showSection();

	void showRequests(List<Request> requests, int offset, int total);

	void showAddForm();

	void showNonBasketOrders(List<Order<?>> orders);

	void showBasketOrders(List<Order<?>> orders);

	void closeAddForm();

	void reloadGrid();

	void showRequest(Request request);

	void confirmDelete();

	void showEditForm(Request request);

	void closeEditForm();

	void notifyTestRequestDone();
}
