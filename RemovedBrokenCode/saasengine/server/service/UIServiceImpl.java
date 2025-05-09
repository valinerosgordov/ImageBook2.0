package ru.saasengine.server.service;

import ru.saasengine.client.model.ui.UIModel;
import ru.saasengine.server.service.beanstore.BeanStoreService;

public class UIServiceImpl implements UIService {
	private final BeanStoreService beanStoreService;

	public UIServiceImpl(BeanStoreService beanStoreService) {
		this.beanStoreService = beanStoreService;
	}

	@Override
	public UIModel getIntegralUIModel() {
		UIModel uiModel = beanStoreService.load("uiModel");
		return uiModel;
	}
}
