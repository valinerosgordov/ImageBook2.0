package ru.imagebook.client.app.view.sent;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

import ru.imagebook.shared.model.Order;


public interface SentView extends IsWidget {
    void showNoOrders();

	void showSentOrders(List<Order<?>> orders);
}
