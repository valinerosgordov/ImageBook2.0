package ru.saasengine.client.ctl.desktop;

import ru.saasengine.client.model.desktop.Menu;
import ru.saasengine.client.model.desktop.MenuItem;

public interface MenuView {
	void showMenu(Menu menu);
	
	void showSubmenu(MenuItem item);

	void hideParentMenus(MenuItem item);
}
