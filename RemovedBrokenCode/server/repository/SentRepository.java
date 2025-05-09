package ru.imagebook.server.repository;

import java.util.List;

import ru.imagebook.shared.model.Order;

public interface SentRepository {
	List<Order<?>> loadOrders(int userId);
}
