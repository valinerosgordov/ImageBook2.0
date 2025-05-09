package ru.saasengine.client.model.ui;

import ru.minogin.core.client.common.Builder;

public class ModelMenuItemBuilder implements Builder<ModelMenuItem> {
	@Override
	public ModelMenuItem newInstance() {
		return new ModelMenuItem();
	}
}
