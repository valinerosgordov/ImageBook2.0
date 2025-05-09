package ru.saasengine.client.model.ui;

import ru.minogin.core.client.common.Builder;

public class AppShortcutBuilder implements Builder<AppShortcut> {
	@Override
	public AppShortcut newInstance() {
		return new AppShortcut();
	}
}
