package ru.imagebook.server.service.flash;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ru.imagebook.shared.model.Order;

public class FlashSession {
	private Date expires;
	private Order<?> order;
	private String logo;
	private Map<String, Object> values = new HashMap<String, Object>();

	public FlashSession(Date expires, Order<?> order) {
		this.expires = expires;
		this.order = order;
	}

	public Date getExpires() {
		return expires;
	}

	public Order<?> getOrder() {
		return order;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public void set(String name, Object value) {
		values.put(name, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String name) {
		return (T) values.get(name);
	}
}
