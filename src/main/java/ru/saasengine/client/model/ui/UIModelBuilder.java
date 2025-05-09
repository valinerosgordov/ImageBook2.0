package ru.saasengine.client.model.ui;

import ru.minogin.core.client.common.Builder;

public class UIModelBuilder implements Builder<UIModel> {
	@Override
	public UIModel newInstance() {
		return new UIModel();
	}
}
