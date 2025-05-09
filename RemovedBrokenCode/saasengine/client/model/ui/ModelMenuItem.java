package ru.saasengine.client.model.ui;

import java.util.ArrayList;
import java.util.List;

import ru.minogin.core.client.bean.BasePersistentIdBean;
import ru.minogin.core.client.i18n.MultiString;

public class ModelMenuItem extends BasePersistentIdBean {
	private static final long serialVersionUID = -5111720216225285260L;

	public static final String TYPE_NAME = "app.ui.XMenuItem";

	private static final String TITLE = "title";
	private static final String ICON = "icon";
	private static final String ACTION = "action";
	private static final String SUBMENU = "submenu";

	ModelMenuItem() {}
	
	ModelMenuItem(ModelMenuItem prototype) {
		super(prototype);
	}
	
	public ModelMenuItem(String id, MultiString title, String icon, Action action) {
		setId(id);
		setTitle(title);
		setIcon(icon);
		setAction(action);
		set(SUBMENU, new ArrayList<ModelMenuItem>());
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	public MultiString getTitle() {
		return get(TITLE);
	}

	public void setTitle(MultiString title) {
		set(TITLE, title);
	}

	public String getIcon() {
		return get(ICON);
	}

	public void setIcon(String icon) {
		set(ICON, icon);
	}

	public Action getAction() {
		return get(ACTION);
	}

	public void setAction(Action action) {
		set(ACTION, action);
	}

	public List<ModelMenuItem> getSubmenu() {
		return get(SUBMENU);
	}
	
	@Override
	public ModelMenuItem copy() {
		return new ModelMenuItem(this);
	}
}
