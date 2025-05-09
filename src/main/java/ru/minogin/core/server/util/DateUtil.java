package ru.minogin.core.server.util;

import java.util.Date;

import org.joda.time.DateTime;

public class DateUtil {
	public static Date createDate(int year, int month, int day) {
		return new DateTime(year, month, day, 0, 0, 0, 0).toDate();
	}
}
