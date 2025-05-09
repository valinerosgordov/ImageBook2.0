package ru.imagebook.server.service.request;

import java.util.List;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Request;
import ru.minogin.core.server.file.TempFile;

public interface RequestService {
	List<Request> loadRequests(int offset, int limit);

	long countRequests();

	List<Order<?>> loadNonBasketOrders();

	long countNonBasketOrders();

	List<Order<?>> loadBasketOrders();

	long countBasketOrders();

	void putToBasket(List<Integer> orderIds);

	void removeFromBasket(List<Integer> orderIds);

	void createRequest(boolean urgentFlag);

	void deleteRequests(List<Integer> requestIds);

	TempFile printRequest(int requestId);

	TempFile bookRequest(int requestId);
	
	TempFile urgentRequest(int requestId);

	TempFile act(int requestId);

	void closeRequests(List<Integer> requestIds);

	TempFile delivery(int requestId);

	void updateRequest(Request request);

	void generateAndSendRequests();

	void sendWeeklyBookRequest();

    void exportLastRequestOrdersToXml();
}
