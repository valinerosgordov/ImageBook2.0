package ru.imagebook.shared.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class MailingType {
	public static final int ALL = 0;
	public static final int NO_PHOTOGRAPHERS = 1;
	public static final int PHOTOGRAPHERS_ONLY = 2;

	public static final Map<Integer, String> values = new LinkedHashMap<Integer, String>();

	static {
		values.put(ALL, "Для всех");
		values.put(NO_PHOTOGRAPHERS, "Для всех кроме фотографов");
		values.put(PHOTOGRAPHERS_ONLY, "Только для фотографов");
	}
}
