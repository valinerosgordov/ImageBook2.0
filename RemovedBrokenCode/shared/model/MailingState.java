package ru.imagebook.shared.model;

import java.util.LinkedHashMap;
import java.util.Map;

import ru.imagebook.client.common.util.i18n.I18n;
import ru.minogin.core.client.i18n.MultiString;

public class MailingState {
	public static final int NEW = 100;
	public static final int SENDING = 200;
	public static final int SENT = 300;

	public static final Map<Integer, MultiString> values = new LinkedHashMap<Integer, MultiString>();

	static {
		values.put(NEW, I18n.ms("Новая", null));
		values.put(SENDING, I18n.ms("Рассылается", null));
		values.put(SENT, I18n.ms("Разослана", null));
	}
}
