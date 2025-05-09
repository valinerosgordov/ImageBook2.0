package ru.imagebook.server.service;

import java.io.Writer;
import java.util.List;

import ru.imagebook.shared.model.Order;
import ru.minogin.core.server.file.TempFile;

public interface DeliveryService {
	List<Order<?>> loadPrintedOrders();

	List<Order<?>> loadDeliveryOrders(Integer deliveryType);

	boolean addOrder(String number);

	void removeOrders(List<Integer> orderIds);

	void print(Integer deliveryType, Writer writer);

	Order<?> findOrder(String number);

	void deliver(int orderId, String code);

	TempFile createPosthouseExcel();

	TempFile createBarcodes();
}
