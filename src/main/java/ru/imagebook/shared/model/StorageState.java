package ru.imagebook.shared.model;

import java.util.LinkedHashMap;
import java.util.Map;

import ru.imagebook.client.common.util.i18n.I18n;
import ru.minogin.core.client.i18n.MultiString;

public class StorageState {
	public static final int STORAGE = 1;
	public static final int DELETED = 2;

	public static final Map<Integer, MultiString> values = new LinkedHashMap<Integer, MultiString>();

	static {
		values.put(STORAGE, I18n.ms("Хранятся", null));
		values.put(DELETED, I18n.ms("Удалены", null));
	}
}
