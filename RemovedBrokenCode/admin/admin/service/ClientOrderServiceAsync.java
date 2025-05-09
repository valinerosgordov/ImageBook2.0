package ru.imagebook.client.admin.service;

import java.util.List;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Vendor;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ClientOrderServiceAsync {
	void export(AsyncCallback<Void> callback);

	void loadVendors(AsyncCallback<List<Vendor>> callback);
	
	void bulkUpdateOrders(List<Order<?>> orders, AsyncCallback<Void> callback);

	void publishOrder(int orderId, AsyncCallback<Integer> callback);
}
