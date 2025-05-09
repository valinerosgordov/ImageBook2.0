package ru.imagebook.server.service.pdf;

import java.util.Collection;
import java.util.List;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.editor.Page;

public interface PdfTxService {
	List<Order<?>> loadPaidOrders();

	List<Page> getPagesFromLayout(final int layoutId);

	void setPdfGenerated(Integer orderId);

	void setPdfErrorState(Integer orderId);

    void updateOrdersState(Collection<Integer> orderIds, int orderState);
}
