package ru.imagebook.server.repository2;

import java.util.List;

import ru.imagebook.shared.model.Order;

public interface PdfTransferRepository {
	List<Order<?>> loadOrdersToTransfer();

	Order<?> getOrder(int orderId);

	List<Order<?>> loadOrdersInProgress();

	int getPackageOrdersQuantityCount(String rootOrderName);
}
