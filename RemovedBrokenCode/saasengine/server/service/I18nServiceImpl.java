package ru.saasengine.server.service;

import ru.saasengine.client.model.i18n.I18nModel;
import ru.saasengine.server.service.beanstore.BeanStoreService;

public class I18nServiceImpl implements I18nService {
	private final BeanStoreService beanStoreService;

	public I18nServiceImpl(BeanStoreService beanStoreService) {
		this.beanStoreService = beanStoreService;
	}

	@Override
	public I18nModel getI18nModel() {
		I18nModel i18nModel = beanStoreService.load("i18nModel");
		return i18nModel;
	}
}
