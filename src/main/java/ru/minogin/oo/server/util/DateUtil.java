package ru.minogin.oo.server.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static Date guessDate(String text) {
		String[] datePatterns = new String[] { "dd.MM.yy", "dd-MM-yy", "dd/MM/yy" };
		String[] timePatterns = new String[] { "HH:mm:ss", "HH:mm", null };

		for (String timePattern : timePatterns) {
			for (String datePattern : datePatterns) {
				String format = datePattern;
				if (timePattern != null)
					format += " " + timePattern;
				Date date = parse(format, text);
				if (date != null)
					return date;
			}
		}

		return null;
	}

	private static Date parse(String pattern, String text) {
		text = text.trim();

		SimpleDateFormat format = new SimpleDateFormat(pattern);

		try {
			return format.parse(text);
		}
		catch (ParseException e) {
			return null;
		}
	}
}
