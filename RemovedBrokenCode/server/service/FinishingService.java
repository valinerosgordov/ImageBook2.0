package ru.imagebook.server.service;

import java.io.OutputStream;
import java.util.List;

import ru.imagebook.shared.model.Order;

public interface FinishingService {
	List<Order<?>> loadOrders();

	void finishOrder(int orderId);

	void scan(int orderId);

	void showPreview(int orderId, OutputStream outputStream);
}
