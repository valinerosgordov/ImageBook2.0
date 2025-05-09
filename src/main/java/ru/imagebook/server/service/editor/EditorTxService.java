package ru.imagebook.server.service.editor;

import java.util.List;

import ru.imagebook.shared.model.Order;

public interface EditorTxService {
	List<Order<?>> loadOrders();

	void setJpegGenerated(Integer orderId);

	void setJpegGenerationError(Integer orderId);
}
