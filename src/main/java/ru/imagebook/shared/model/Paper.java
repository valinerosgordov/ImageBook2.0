package ru.imagebook.shared.model;

import java.util.LinkedHashMap;
import java.util.Map;

import ru.imagebook.client.common.util.i18n.I18n;
import ru.minogin.core.client.i18n.MultiString;

public class Paper {
	public static final int EVERFLAT = 1;

	public static final Map<Integer, MultiString> values = new LinkedHashMap<Integer, MultiString>();

	static {
		values.put(EVERFLAT, I18n.ms("Everflat", null));
		values.put(2, I18n.ms("Colotech 200", null));
		values.put(3, I18n.ms("Colotech 250", null));
		values.put(4, I18n.ms("Colotech 300", null));
		values.put(5, I18n.ms("Основной блок — Colotech 250, подложка — картон 300", null));
		values.put(6, I18n.ms("Основной блок — офсет 80, постер  — Colotech 300, подложка — картон 300",
				null));
	}
}
