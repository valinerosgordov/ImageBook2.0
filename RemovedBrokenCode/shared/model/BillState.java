package ru.imagebook.shared.model;

import java.util.LinkedHashMap;
import java.util.Map;

import ru.imagebook.client.common.util.i18n.I18n;
import ru.minogin.core.client.i18n.MultiString;

public class BillState {
	public static final int NEW = 100;
	public static final int PAID = 200;

	public static final Map<Integer, MultiString> values = new LinkedHashMap<Integer, MultiString>();

	static {
		values.put(NEW, I18n.ms("Новый", null));
		values.put(PAID, I18n.ms("Оплачен", null));
	}
}
