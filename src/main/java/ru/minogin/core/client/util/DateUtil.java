package ru.minogin.core.client.util;

import java.util.Date;

public interface DateUtil {
	Date guess(String text);

	Date parse(String pattern, String text);

	Date dayStart(Date date);

	Date dayEnd(Date date);

	Date minuteStart(Date date);
}
