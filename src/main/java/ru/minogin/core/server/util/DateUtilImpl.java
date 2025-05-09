package ru.minogin.core.server.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ru.minogin.core.client.util.DateUtil;

public class DateUtilImpl implements DateUtil {
	@Override
	public Date parse(String pattern, String text) {
		text = text.trim();

		SimpleDateFormat format = new SimpleDateFormat(pattern);

		try {
			return format.parse(text);
		}
		catch (ParseException e) {
			return null;
		}
	}

	@Override
	public Date guess(String text) {
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

	@Override
	public Date dayStart(Date date) {
		if (date == null)
			return null;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	@Override
	public Date dayEnd(Date date) {
		if (date == null)
			return null;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	@Override
	public Date minuteStart(Date date) {
		if (date == null)
			return null;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
}
