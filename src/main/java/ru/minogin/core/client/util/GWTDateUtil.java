package ru.minogin.core.client.util;

import java.util.Date;

import ru.minogin.core.client.NotImplementedException;

import com.extjs.gxt.ui.client.util.DateWrapper;

public class GWTDateUtil implements DateUtil {
	@Override
	public Date guess(String text) {
		throw new NotImplementedException();
	}

	@Override
	public Date parse(String pattern, String text) {
		throw new NotImplementedException();
	}

	@Override
	public Date dayStart(Date date) {
		DateWrapper dateWrapper = new DateWrapper(date);
		dateWrapper = dateWrapper.clearTime();
		return dateWrapper.asDate();
	}

	@Override
	public Date dayEnd(Date date) {
		DateWrapper dateWrapper = new DateWrapper(date);
		dateWrapper = dateWrapper.clearTime();
		dateWrapper = dateWrapper.addDays(1);
		return dateWrapper.asDate();
	}

	@Override
	public Date minuteStart(Date date) {
		throw new NotImplementedException();
	}
}
