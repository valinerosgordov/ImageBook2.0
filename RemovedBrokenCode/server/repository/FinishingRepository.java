package ru.imagebook.server.repository;

import java.util.List;

import ru.imagebook.shared.model.Order;

public interface FinishingRepository {
	List<Order<?>> loadOrders();

	Order<?> findOrder(int orderId);

	Order<?> findPrintingOrder(int orderId);
}
