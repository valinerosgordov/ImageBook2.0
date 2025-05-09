package ru.imagebook.server.repository;

import java.util.List;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Request;

public interface RequestRepository {
	List<Request> loadRequests(int offset, int limit);

	long countRequests();

	List<Order<?>> loadNonBasketOrders();

	long countNonBasketOrders();

	List<Order<?>> loadBasketOrders();

	long countBasketOrders();

	List<Order<?>> loadOrders(List<Integer> orderIds);

	List<Order<?>> loadAllBasketOrders();

	void saveRequest(Request request);

	int getMaxRequestNumber();

	Request getRequest(int requestId);

	List<Request> loadRequests(List<Integer> requestIds);

	void deleteRequests(List<Integer> requestIds);

	List<Order<?>> loadTodaysPrintingOrders();
	
    List<Order<?>> loadLastRequestOrders();

	List<Order<?>> loadLastWeekOrders();
}
