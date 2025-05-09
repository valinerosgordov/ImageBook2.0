package ru.imagebook.shared.model;

public class AdminSettings extends Settings {
	private static final long serialVersionUID = 7693786799395818299L;

	public static final String ORDER_FILTER = "orderFilter";

	public AdminSettings() {
		setOrderFilter(new OrderFilter());
	}

	public OrderFilter getOrderFilter() {
		return get(ORDER_FILTER);
	}

	public void setOrderFilter(OrderFilter orderFilter) {
		set(ORDER_FILTER, orderFilter);
	}
}
