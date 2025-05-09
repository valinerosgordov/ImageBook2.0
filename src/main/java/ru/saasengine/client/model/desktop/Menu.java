package ru.saasengine.client.model.desktop;

import ru.minogin.core.client.bean.BaseBean;
import ru.minogin.core.client.model.IdMap;

public class Menu extends BaseBean {
	private static final long serialVersionUID = -7492364411300588733L;

	private static final String ITEMS = "items";

	public Menu() {
		setItems(new IdMap<MenuItem>());
	}

	public IdMap<MenuItem> getItems() {
		return get(ITEMS);
	}

	private void setItems(IdMap<MenuItem> items) {
		set(ITEMS, items);
	}

	public void addItem(MenuItem item) {
		getItems().add(item);
	}
	
	public boolean isEmpty() {
		return getItems().isEmpty();
	}
}
