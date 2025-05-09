package ru.imagebook.client.common.service;

import java.util.Collection;

import ru.minogin.core.client.collections.XArrayList;
import ru.minogin.core.client.i18n.locale.Locales;

import com.google.inject.Singleton;

@Singleton
public class I18nService {
	public String getLocale() {
		return Locales.RU;
	}

	public Collection<String> getLocales() {
		return new XArrayList<String>(Locales.RU, Locales.EN);
	}
}
