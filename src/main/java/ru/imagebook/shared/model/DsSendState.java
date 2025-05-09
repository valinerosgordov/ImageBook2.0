package ru.imagebook.shared.model;

import ru.imagebook.client.common.util.i18n.I18n;
import ru.minogin.core.client.i18n.MultiString;

import java.util.LinkedHashMap;
import java.util.Map;

public class DsSendState {
	public static final int NOT_SENT = 100;
	public static final int FAILURE = 200;
	public static final int SENT = 300;

	public static final Map<Integer, MultiString> values = new LinkedHashMap<Integer, MultiString>();

	static {
		values.put(NOT_SENT, I18n.ms("Не передан", null));
		values.put(FAILURE, I18n.ms("Ошибка", null));
		values.put(SENT, I18n.ms("Передан", null));
	}
}
