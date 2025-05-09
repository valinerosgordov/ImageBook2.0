package ru.imagebook.server.repository;

import java.util.List;

import ru.imagebook.shared.model.Order;

public interface ProcessRepository {
	List<Order<?>> loadOrders(int userId);
}
