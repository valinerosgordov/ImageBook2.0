package ru.imagebook.server.tools;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class DateUtil {
	private DateUtil() {
	}
	
	public static Date firstDayOfLastWeek(Calendar c) {
	    c = (Calendar) c.clone();
	    // last week
	    c.add(Calendar.WEEK_OF_YEAR, -1);
	    // first day
	    c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
	    c = DateUtils.truncate(c, Calendar.DATE);
	    return c.getTime();
	}

	public static Date lastDayOfLastWeek(Calendar c) {
	    c = (Calendar) c.clone();
	    // first day of this week
	    c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
	    // last day of previous week
	    c.add(Calendar.DAY_OF_MONTH, -1);
	    c = DateUtils.truncate(c, Calendar.DATE);
	    return c.getTime();
	}

	public static Date getAlignedStartDate(final Date startDate) {
		if (startDate == null) return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		return getAlignedStartDate(calendar);
	}

	public static Date getAlignedStartDate(final Calendar calendar) {
		if (calendar == null) return null;
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	public static Date getAlignedEndDate(final Calendar calendar) {
		if (calendar == null) return null;
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}

	public static Date getAlignedEndDate(final Date endDate) {
		if (endDate == null) return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		return getAlignedEndDate(calendar);
	}
	
	public static Date setHour(Date date, int hourOfDay) {
        Date d = DateUtils.setHours(date, hourOfDay);       
        return DateUtils.truncate(d, Calendar.HOUR);
    }
}
