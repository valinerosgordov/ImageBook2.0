package ru.imagebook.shared.model;

import java.util.LinkedHashMap;
import java.util.Map;

import ru.imagebook.client.common.util.i18n.I18n;
import ru.minogin.core.client.i18n.MultiString;

public class CoverLamination {
	public static final int NONE = 0;
	public static final int GLOSSY = 1;
	public static final int MATTE = 2;
	public static final int SAND = 3;

	public static final Map<Integer, MultiString> values = new LinkedHashMap<Integer, MultiString>();
	public static final Map<Integer, String> shorts = new LinkedHashMap<Integer, String>();
	public static final Map<Integer, String> shortValues = new LinkedHashMap<Integer, String>();

	static {
		values.put(NONE, I18n.ms("Нет", null));
		values.put(GLOSSY, I18n.ms("Глянцевая", null));
		values.put(MATTE, I18n.ms("Матовая", null));
		values.put(SAND, I18n.ms("Песок", null));

		shorts.put(NONE, null);
		shorts.put(GLOSSY, "ОГ");
		shorts.put(MATTE, "ОМ");
		shorts.put(SAND, "ОП");

		shortValues.put(NONE, "");
		shortValues.put(GLOSSY, "Гл");
		shortValues.put(MATTE, "Мат");
		shortValues.put(SAND, "Пес");
	}
}
