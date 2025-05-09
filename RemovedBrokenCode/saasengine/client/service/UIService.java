package ru.saasengine.client.service;

import ru.saasengine.client.model.ui.UIModel;

import com.google.inject.Singleton;

@Singleton
public class UIService {
	private UIModel uiModel;

	public UIModel getUiModel() {
		return uiModel;
	}

	public void setUiModel(UIModel uiModel) {
		this.uiModel = uiModel;
	}
}
