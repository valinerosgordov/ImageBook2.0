package ru.imagebook.shared.model;

import java.util.LinkedHashMap;
import java.util.Map;

import ru.imagebook.client.common.util.i18n.I18n;
import ru.minogin.core.client.i18n.MultiString;

public class Cover {
	public static final int NONE = 0;
	public static final int PLOTTER_COVER = 2;
	public static final int RICOH_COVER = 5;
	public static final int LEATHERETTE_COVER = 6;

	public static final Map<Integer, MultiString> values = new LinkedHashMap<Integer, MultiString>();

	static {
		values.put(NONE, I18n.ms("Нет", null));
		values.put(1, I18n.ms("\"Белая\", ламинированная", null));
		values.put(PLOTTER_COVER, I18n.ms("Плоттерная ламинированная", null));
		values.put(3, I18n.ms("Кожа/тряпка/синтетика", null));
		values.put(4, I18n.ms("Пластик / \"Папка\"", null));
		values.put(RICOH_COVER, I18n.ms("Ricoh 9200", null));
		values.put(LEATHERETTE_COVER, I18n.ms("Кожзам", null));
	}
}
