package ru.imagebook.client.flash.service;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.User;
import ru.minogin.core.client.rpc.Transportable;

public class FlashData implements Transportable {
	private Order<?> order;
	private User user;

	FlashData() {}

	public FlashData(Order<?> order, User user) {
		this.order = order;
		this.user = user;
	}

	public Order<?> getOrder() {
		return order;
	}

	public User getUser() {
		return user;
	}
}
