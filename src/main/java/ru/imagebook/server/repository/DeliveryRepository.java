package ru.imagebook.server.repository;

import java.util.Collection;
import java.util.List;

import ru.imagebook.shared.model.Order;

public interface DeliveryRepository {
	List<Order<?>> loadPrintedOrders();

	List<Order<?>> loadDeliveryOrders(Integer deliveryType);

	List<Order<?>> loadOrdersFromBuffer(List<Integer> orderIds);

	Order<?> findOrder(String number);

	Order<?> findOrder(int orderId);

	Collection<Integer> loadPostBillIds();
}
