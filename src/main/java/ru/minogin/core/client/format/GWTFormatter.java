package ru.minogin.core.client.format;

import com.google.gwt.i18n.client.NumberFormat;

public class GWTFormatter implements Formatter {
	@Override
	public String n2(int n) {
		NumberFormat format = NumberFormat.getFormat("00");
		return format.format(n);
	}
}
