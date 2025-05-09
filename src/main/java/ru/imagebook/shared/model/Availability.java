package ru.imagebook.shared.model;

import java.util.LinkedHashMap;
import java.util.Map;

import ru.imagebook.client.common.util.i18n.I18n;
import ru.minogin.core.client.i18n.MultiString;

public class Availability {
	public static final int NOT_PRESENT = 0;
	public static final int DEVELOPMENT = 1;
	public static final int PRESENT = 2;
	
	public static final Map<Integer, MultiString> values = new LinkedHashMap<Integer, MultiString>();

	static {
		values.put(NOT_PRESENT, I18n.ms("Нет", null));
		values.put(DEVELOPMENT, I18n.ms("В разработке", null));
		values.put(PRESENT, I18n.ms("Есть", null));
	}
}
