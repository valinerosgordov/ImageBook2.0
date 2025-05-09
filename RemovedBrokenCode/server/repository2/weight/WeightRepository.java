package ru.imagebook.server.repository2.weight;

import java.util.List;

import ru.imagebook.shared.model.Order;

public interface WeightRepository {
	List<Order<?>> loadYearOrders();
}
