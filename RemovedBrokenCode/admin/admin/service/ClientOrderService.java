package ru.imagebook.client.admin.service;

import java.util.List;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Vendor;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("order.remoteService")
public interface ClientOrderService extends RemoteService {
	void export();

	List<Vendor> loadVendors();
	
	void bulkUpdateOrders(List<Order<?>> orders);

	Integer publishOrder(int orderId);
}
