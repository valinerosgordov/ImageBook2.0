package ru.imagebook.shared.model;

import java.util.LinkedHashMap;
import java.util.Map;

import ru.imagebook.client.common.util.i18n.I18n;
import ru.minogin.core.client.i18n.MultiString;

public class Binding {
	public static final int NONE = 0;

	public static final Map<Integer, MultiString> values = new LinkedHashMap<Integer, MultiString>();
	public static final Map<Integer, String> shorts = new LinkedHashMap<Integer, String>();

	static {
		values.put(NONE, I18n.ms("Нет", null));
		values.put(1, I18n.ms("Втачку", null));
		values.put(2, I18n.ms("Пружина", null));
		values.put(3, I18n.ms("Скрепка", null));

		shorts.put(NONE, null);
		shorts.put(1, "ВТАЧКУ");
		shorts.put(2, "ПРУЖИНА");
		shorts.put(3, "СКРЕПКА");
	}
}
