package ru.minogin.core.client.gwt;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;

public class DateFormat {
	public static String formatDate(Date date) {
		if (date != null)
			return DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM).format(date);
		else
			return "";
	}

	public static String formatDateTime(Date date) {
		if (date != null)
			return DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM).format(date) + " "
					+ DateTimeFormat.getFormat(PredefinedFormat.TIME_SHORT).format(date);
		else
			return "";
	}

	public static String formatTime(Date date) {
		if (date != null)
			return DateTimeFormat.getFormat(PredefinedFormat.TIME_SHORT).format(date);
		else
			return "";
	}
}
