package ru.imagebook.client.editor.ctl.order;

import java.util.List;
import java.util.Map;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Product;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadProductsResultMessage extends BaseMessage {
	private static final long serialVersionUID = -2636847084919041925L;

	public static final String PRODUCTS = "products";
	public static final String ORDERS = "orders";

	LoadProductsResultMessage() {}

	public LoadProductsResultMessage(Map<Integer, List<Product>> products, List<Order<?>> orders) {
		super(OrderMessages.LOAD_PRODUCTS_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(PRODUCTS, products);
		set(ORDERS, orders);
	}

	public Map<Integer, List<Product>> getProducts() {
		return get(PRODUCTS);
	}

	public List<Order<?>> getOrders() {
		return get(ORDERS);
	}
}
