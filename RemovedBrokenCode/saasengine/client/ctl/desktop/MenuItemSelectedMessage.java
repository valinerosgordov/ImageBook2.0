package ru.saasengine.client.ctl.desktop;

import ru.minogin.core.client.flow.BaseMessage;
import ru.saasengine.client.model.desktop.MenuItem;

public class MenuItemSelectedMessage extends BaseMessage {
	private static final long serialVersionUID = -2029146668473915428L;

	public static final String ITEM = "item";
	
	MenuItemSelectedMessage() {}

	public MenuItemSelectedMessage(MenuItem item) {
		super(DesktopMessages.MENU_ITEM_SELECTED);

		set(ITEM, item);
	}

	public MenuItem getItem() {
		return get(ITEM);
	}
}
