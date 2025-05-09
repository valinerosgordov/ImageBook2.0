package ru.saasengine.client.service;

import java.util.List;

import ru.saasengine.client.model.desktop.Menu;
import ru.saasengine.client.model.desktop.MenuItem;
import ru.saasengine.client.model.ui.ModelMenuItem;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class MenuService {
	private final I18nService i18nService;

	private Menu menu;

	@Inject
	public MenuService(I18nService i18nService) {
		this.i18nService = i18nService;
	}

	public void applyModelMenu(List<ModelMenuItem> modelItems) {
		menu = new Menu();
		
		String locale = i18nService.getLocale();
		for (ModelMenuItem modelItem : modelItems) {
			MenuItem item = createItem(locale, modelItem);
			menu.addItem(item);
		}
	}

	private MenuItem createItem(String locale, ModelMenuItem modelItem) {
		String name = modelItem.getTitle().getNonEmptyValue(locale);
		MenuItem item = new MenuItem(modelItem.getId(), name);
		item.setAction(modelItem.getAction());

		for (ModelMenuItem modelSubItem : modelItem.getSubmenu()) {
			item.getSubmenu().addItem(createItem(locale, modelSubItem));
		}

		return item;
	}
	
	public Menu getMenu() {
		return menu;
	}
}
