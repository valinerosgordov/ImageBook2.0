package ru.minogin.core.client.gwt.i18n;

import com.google.gwt.core.client.GWT;

public class I18nService {
	private I18nConstants constants = GWT.create(I18nConstants.class);

	public String getLocale() {
		return constants.locale();
	}
}
