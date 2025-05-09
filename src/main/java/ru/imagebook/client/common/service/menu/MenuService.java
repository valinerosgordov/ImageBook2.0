package ru.imagebook.client.common.service.menu;

import ru.imagebook.client.common.service.vendor.VendorService;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.VendorType;
import ru.saasengine.client.model.desktop.Menu;
import ru.saasengine.client.model.desktop.MenuItem;

import com.google.inject.Inject;

public class MenuService {
	private final MenuConstants constants;
	private final VendorService vendorService;

	private Menu menu = new Menu();

	@Inject
	public MenuService(MenuConstants constants, VendorService vendorService) {
		this.constants = constants;
		this.vendorService = vendorService;
	}

	public Menu getMenu() {
		Vendor vendor = vendorService.getVendor();

		menu.addItem(new MenuItem(MenuItems.NEW_ORDERS, constants.orders()));
		menu.addItem(new MenuItem(MenuItems.BILLS, constants.bills()));
		menu.addItem(new MenuItem(MenuItems.ORDERS_PROCESSED, constants
				.processingOrders()));
		menu.addItem(new MenuItem(MenuItems.ORDERS_SENT, constants.ordersArchive()));

		menu.addItem(new MenuItem(MenuItems.PERSONAL, constants.personal()));
		if (vendor.getType() == VendorType.IMAGEBOOK) {
			menu.addItem(new MenuItem(MenuItems.EDITOR, constants.editor()));
		}
		menu.addItem(new MenuItem(MenuItems.SUPPORT, constants.support()));
		menu.addItem(new MenuItem(MenuItems.LOGOUT, constants.logout()));

		return menu;
	}
}
