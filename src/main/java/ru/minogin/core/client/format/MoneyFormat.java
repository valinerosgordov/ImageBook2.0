package ru.minogin.core.client.format;

import java.math.BigDecimal;

import com.google.gwt.i18n.client.LocaleInfo;

public class MoneyFormat {
	public static String format(BigDecimal value) {
		String separator = LocaleInfo.getCurrentLocale().getNumberConstants().monetarySeparator();
		return value.toString().replace(".", separator);
	}
}
