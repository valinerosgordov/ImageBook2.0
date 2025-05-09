package ru.imagebook.server.repository;

import java.util.List;

import ru.imagebook.shared.model.Order;

public interface FlashRepository {
	List<Order<?>> loadOrders();
	
	Order<?> getOrder(int orderId);

	Order<?> findOrder(String number);
}
