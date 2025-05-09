package ru.saasengine.client.model.i18n;

import ru.minogin.core.client.common.Builder;

public class CustomLocaleBuilder implements Builder<I18nModel> {
	@Override
	public I18nModel newInstance() {
		return new I18nModel();
	}
}
