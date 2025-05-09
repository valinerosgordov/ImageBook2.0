package ru.imagebook.server.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.BonusCode;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderFilter;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.pricing.PricingData;
import ru.minogin.core.server.file.TempFile;

public interface OrderService {
	String PRICING_DATA = "pricingData";

	Order<?> getOrder(int id);

	void save(Bill bill);

	List<Bill> loadBills(int userId);

	Bill getBill(int id);

	Integer computePrice(Order<?> order);

	Integer computeCost(Order<?> order, int productQuantity);

	/** This methods should never be called after an order was paid. */
	void setPricesAndCosts(Order<?> order);
	
	/** This methods should never be called after an order was paid. */
	void setPricesAndCosts(Collection<Order<?>> orders);
	
	Map<Integer, Integer> calculateProductQuantity(Collection<Order<?>> basketOrders);

	TempFile generateReceipt(Bill bill, int format, String name, String address);

	void loadPricingData();

	Integer computePhPrice(Order<?> order);

	List<Order<?>> loadOrders(int offset, int limit, OrderFilter filter, String query);

	long countOrders(OrderFilter filter, String query);

	Map<Integer, List<Product>> loadProductsMap();

	List<User> loadUsers(int offset, int limit, String query);

	long countUsers(String query);

	void deleteOrders(List<Integer> orderIds);

	void addOrder(Order<?> order);

	void updateOrder(Order<?> order);
	
	void notifyUserOrderRejected(int orderId);

	void applyFilter(int userId, OrderFilter filter);

	void setOrderParams(int orderId, Integer colorId, Integer coverLam,
			Integer pageLam);

	void setComputedValues(Bill bill);

	void applyCode(int orderId, String code, String deactivationCode, User user);

	void notifyNewOrder(List<Integer> orderIds);

	void notifyFlashGenerated(List<Integer> orderIds);

	Order<?> orderTrial(int orderId, User user);

	void qiwiPay(int billId, User user);

	void notifyAdmin(AlbumOrder order);

	PricingData getPricingData();

	void setFreeIfIsFirstTrial(AlbumOrder order);

	void deleteBillSecure(int billId, int userId);

	void deleteBill(Bill bill);

	void createBillFromOrder(Order<?> order);

	void notifyNewBill(Bill bill);

	void notifyNewUserOrder(int orderId);
	
	void notifyAddressCommentSpecified(Collection<Order<?>> orders, Address address);
	
	void notifyOrderCommentSpecified(Order<?> order);

	void computeOrderPrintDate(Order<?> order);

    void flush();

	BonusCode getBonusCode(String code, User user);

	/**
	 * Опрос таблицы bill на наличие даты в поле pickup_send_state_date. Если поле не пустое и текущая дата - 30 минут >= значению в этом поле,
	 * тогда отправляем пользователю уведомление для самовывоза
	 * */
	void callNotificationPickupDelivery();

	Order<?> getOrderByPublishCode(int publishCode);

	Integer publishOrder(int orderId);
}