package ru.saasengine.client.model.ui;

import ru.minogin.core.client.bean.BasePersistentBean;
import ru.minogin.core.client.i18n.MultiString;

public class AppShortcut extends BasePersistentBean {
	private static final long serialVersionUID = -2251720524335811855L;

	public static final String TYPE_NAME = "app.ui.XShortcut";

	private static final String TITLE = "title";
	private static final String ICON = "icon";
	private static final String ACTION = "action";

	AppShortcut() {}

	public AppShortcut(AppShortcut prototype) {
		super(prototype);
	}

	public AppShortcut(MultiString title, String icon, Action action) {
		set(TITLE, title);
		set(ICON, icon);
		set(ACTION, action);
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	public MultiString getTitle() {
		return get(TITLE);
	}

	public String getIcon() {
		return get(ICON);
	}

	public Action getAction() {
		return get(ACTION);
	}

	@Override
	public AppShortcut copy() {
		return new AppShortcut(this);
	}
}
