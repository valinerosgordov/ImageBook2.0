package ru.imagebook.shared.model;

import java.util.LinkedHashMap;
import java.util.Map;

import ru.imagebook.client.common.util.i18n.I18n;
import ru.minogin.core.client.i18n.MultiString;

public class InvitationState {
	public static final int NOT_SENT = 100;
	public static final int SENT = 200;
	public static final int CONFIRMED = 300;
	public static final int ERROR_WHEN_SENDING = 400;
	public static final int ERROR_WHEN_CONFIRMED = 500;

	public static final Map<Integer, MultiString> values = new LinkedHashMap<Integer, MultiString>();

	static {
		values.put(NOT_SENT, I18n.ms("-", null));
		values.put(SENT, I18n.ms("Отправлено", null));
		values.put(CONFIRMED, I18n.ms("Подтверждено", null));
		values.put(ERROR_WHEN_SENDING, I18n.ms("Ошибка при отправке", null));
		values.put(ERROR_WHEN_CONFIRMED, I18n.ms("Ошибка при подтверждении", null));
	}
}
