package ru.imagebook.server.repository2;

import ru.imagebook.shared.model.Order;

public interface FlashRepository {
	Order<?> getOrder(int orderId);

	Order<?> getOrder(String orderCode);
}
