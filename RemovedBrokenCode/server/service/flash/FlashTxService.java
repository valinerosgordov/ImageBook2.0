package ru.imagebook.server.service.flash;

import java.util.List;

import ru.imagebook.shared.model.Order;

public interface FlashTxService {
	List<Order<?>> loadOrders();

	void setFlashGenerated(Integer id);

	void notifyFlashGenerated(Order<?> order);
}
