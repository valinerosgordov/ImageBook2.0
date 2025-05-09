package ru.imagebook.server.service.pickbook;

import ru.imagebook.server.model.importing.XOrder;


public interface PickbookOrderService {
	void createOrder(XOrder xOrder);

    void updateOrder(XOrder xOrder);

    void orderRenderStarted(XOrder xOrder);

    void orderRenderFinished(XOrder xOrder);
}
