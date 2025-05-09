package ru.saasengine.client.model.i18n;

import java.util.*;

import ru.minogin.core.client.bean.BasePersistentBean;

public class I18nModel extends BasePersistentBean {
	private static final long serialVersionUID = 7483106739779370446L;

	public static final String TYPE_NAME = "I18nModel";

	private static final String SYSTEM_LOCALES = "systemLocales";
	private static final String CUSTOM_LOCALES = "customLocales";

	I18nModel() {}

	I18nModel(I18nModel prototype) {
		super(prototype);
	}

	public I18nModel(Set<String> locales) {
		setSystemLocales(locales);
		set(CUSTOM_LOCALES, new LinkedHashMap<String, CustomLocale>());
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	public Set<String> getSystemLocales() {
		return get(SYSTEM_LOCALES);
	}

	public void setSystemLocales(Set<String> locales) {
		set(SYSTEM_LOCALES, locales);
	}

	public Map<String, CustomLocale> getCustomLocales() {
		return get(CUSTOM_LOCALES);
	}

	public void addCustomLocale(CustomLocale locale) {
		getCustomLocales().put(locale.getCode(), locale);
	}

	@Override
	public I18nModel copy() {
		return new I18nModel(this);
	}
}
