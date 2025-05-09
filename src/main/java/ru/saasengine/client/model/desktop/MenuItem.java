package ru.saasengine.client.model.desktop;

import ru.minogin.core.client.bean.BaseIdBean;
import ru.saasengine.client.model.ui.Action;

public class MenuItem extends BaseIdBean {
	private static final long serialVersionUID = -7452521461478283217L;

	private static final String NAME = "name";
	private static final String SUBMENU = "submenu";
	private static final String ACTION = "action";

	public MenuItem(String id, String name) {
		super(id);

		setName(name);
		setSubmenu(new Menu());
	}

	public String getName() {
		return get(NAME);
	}

	public void setName(String name) {
		set(NAME, name);
	}

	public boolean hasSubmenu() {
		return !getSubmenu().isEmpty();
	}

	public Menu getSubmenu() {
		return get(SUBMENU);
	}

	public void setSubmenu(Menu menu) {
		set(SUBMENU, menu);
	}

	public Action getAction() {
		return get(ACTION);
	}

	public void setAction(Action action) {
		set(ACTION, action);
	}
}
