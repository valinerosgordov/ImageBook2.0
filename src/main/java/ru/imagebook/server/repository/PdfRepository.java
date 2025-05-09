package ru.imagebook.server.repository;

import java.util.Collection;
import java.util.List;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.editor.Page;

public interface PdfRepository {
	List<Order<?>> loadPaidOrders();

	List<Order<?>> loadOrders(Collection<Integer> ids);

	Order<?> getOrder(int orderId);

	List<Page> getPagesFromLayout(int layoutId);
}
