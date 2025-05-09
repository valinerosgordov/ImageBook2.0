package ru.saasengine.client.model.ui;

import java.util.*;

import ru.minogin.core.client.bean.BasePersistentBean;

public class UIModel extends BasePersistentBean {
	private static final long serialVersionUID = 8986670151586366508L;

	public static final String TYPE_NAME = "UIModel";

	private static final String MENU_ITEMS = "menuItems";
	private static final String VISIBLE_COLUMNS = "visibleColumns";
	private static final String TYPE_ICONS = "typeIcons";
	private static final String TABS = "tabs";

	public UIModel() {
		set(MENU_ITEMS, new ArrayList<ModelMenuItem>());
		set(VISIBLE_COLUMNS, new HashMap<String, Set<String>>());
		set(TYPE_ICONS, new HashMap<String, String>());
		set(TABS, new HashMap<String, Collection<String>>());
	}

	public UIModel(UIModel prototype) {
		super(prototype);
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	public void add(UIModel model) {
		getMenuItems().addAll(model.getMenuItems());
		getVisibleColumns().putAll(model.getVisibleColumns());
		getTypeIcons().putAll(model.getTypeIcons());
		getTabs().putAll(model.getTabs());
	}

	public void remove(UIModel model) {
		for (ModelMenuItem modelItem : model.getMenuItems()) {
			for (ModelMenuItem item : getMenuItems()) {
				if (item.getId().equals(modelItem.getId())) {
					getMenuItems().remove(item);
					break;
				}
			}
		}

		for (String classId : model.getVisibleColumns().keySet()) {
			getVisibleColumns().remove(classId);
		}

		for (String classId : model.getTypeIcons().keySet()) {
			getTypeIcons().remove(classId);
		}

		for (String typeId : model.getTabs().keySet()) {
			getTabs().remove(typeId);
		}
	}

	public List<ModelMenuItem> getMenuItems() {
		return get(MENU_ITEMS);
	}

	public Map<String, Set<String>> getVisibleColumns() {
		return get(VISIBLE_COLUMNS);
	}

	public Map<String, String> getTypeIcons() {
		return get(TYPE_ICONS);
	}

	public Map<String, Collection<String>> getTabs() {
		return get(TABS);
	}

	public Collection<String> getTabs(String typeId) {
		return getTabs().get(typeId);
	}

	public void setTabs(String typeId, Collection<String> typeTabs) {
		getTabs().put(typeId, typeTabs);
	}

	public void addTypeIcons(Map<String, String> typeIcons) {
		getTypeIcons().putAll(typeIcons);
	}

	@Override
	public UIModel copy() {
		return new UIModel(this);
	}
}
