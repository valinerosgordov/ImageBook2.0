package ru.saasengine.client.service;

import com.google.inject.Singleton;

@Singleton
public class I18nService {
	private String locale;

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
}
