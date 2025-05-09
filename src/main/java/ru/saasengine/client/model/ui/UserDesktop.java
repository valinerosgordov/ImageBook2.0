package ru.saasengine.client.model.ui;

import java.util.ArrayList;
import java.util.List;

import ru.minogin.core.client.bean.BasePersistentBean;

public class UserDesktop extends BasePersistentBean {
	private static final long serialVersionUID = -7615958818473268620L;

	public static final String TYPE_NAME = "app.ui.UserDesktop";

	private static final String SHORTCUTS = "shortcuts";

	public UserDesktop() {
		set(SHORTCUTS, new ArrayList<AppShortcut>());
	}

	UserDesktop(UserDesktop prototype) {
		super(prototype);
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	public List<AppShortcut> getShortcuts() {
		return get(SHORTCUTS);
	}

	@Override
	public UserDesktop copy() {
		return new UserDesktop(this);
	}
}
