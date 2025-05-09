package ru.imagebook.shared.model;

import java.util.LinkedHashMap;
import java.util.Map;

import ru.imagebook.client.common.util.i18n.I18n;
import ru.minogin.core.client.i18n.MultiString;

public class StatusRequestState {
	public static final int NEW = 100;
	public static final int APPROVED = 200;
	public static final int REJECTED = 300;
	public static final int ACTIVATED = 400;

	public static final Map<Integer, MultiString> values = new LinkedHashMap<Integer, MultiString>();

	static {
		values.put(NEW, I18n.ms("Новая", null));
		values.put(APPROVED, I18n.ms("Код выслан", null));
		values.put(REJECTED, I18n.ms("Отклонена", null));
		values.put(ACTIVATED, I18n.ms("Статус присвоен", null));
	}
}
