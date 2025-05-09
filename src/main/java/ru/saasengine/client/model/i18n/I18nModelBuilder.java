package ru.saasengine.client.model.i18n;

import ru.minogin.core.client.common.Builder;

public class I18nModelBuilder implements Builder<CustomLocale> {
	@Override
	public CustomLocale newInstance() {
		return new CustomLocale();
	}
}
