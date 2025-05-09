package ru.minogin.core.client.gxt.form;

import java.util.Date;

import ru.minogin.core.client.gwt.DateFormat;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.util.DateWrapper;
import com.extjs.gxt.ui.client.widget.form.Time;
import com.extjs.gxt.ui.client.widget.form.TimeField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

public class XTimeField extends TimeField {
	private Date date;
	private boolean focused;

	public XTimeField() {
		setTriggerAction(TriggerAction.ALL);
	}

	@Override
	protected void initList() {
		super.initList();

		store.removeAll();

		DateWrapper date = new DateWrapper();
		date = date.clearTime();
		date = date.addHours(8);

		DateWrapper maxDate = new DateWrapper();
		maxDate = maxDate.clearTime();
		maxDate = maxDate.addHours(23);
		maxDate = maxDate.addMinutes(59);

		while (date.before(maxDate)) {
			store.add(new Time(date.asDate(), getFormat().format(date.asDate())));
			date = date.addMinutes(getIncrement());
		}

		date = new DateWrapper();
		date = date.clearTime();

		maxDate = new DateWrapper();
		maxDate = maxDate.clearTime();
		maxDate = maxDate.addHours(8);

		while (date.before(maxDate)) {
			store.add(new Time(date.asDate(), getFormat().format(date.asDate())));
			date = date.addMinutes(getIncrement());
		}
	}

	@Override
	public Time getValue() {
		value = null;

		String rawValue = getRawValue();
		if (rawValue != null && !rawValue.isEmpty()) {
			try {
				value = new Time(getFormat().parse(rawValue));
			}
			catch (IllegalArgumentException e) {
			}
		}
		return value;
	}

	@Override
	public void setDateValue(Date date) {
		if (date != null)
			super.setDateValue(date);
		else
			setValue(null);

		this.date = date;
		setRawValue(DateFormat.formatTime(date));
	}

	@Override
	public void render(Element target, int index) {
		super.render(target, index);

		if (date != null)
			setRawValue(DateFormat.formatTime(date));
	}

	public boolean isFocused() {
		return focused;
	}

	@Override
	protected void onBlur(ComponentEvent ce) {
		super.onBlur(ce);

		focused = false;
	}

	@Override
	public void onComponentEvent(ComponentEvent ce) {
		if (ce.getEventTypeInt() == Event.ONMOUSEDOWN)
			focused = true;

		super.onComponentEvent(ce);
	}

	@Override
	protected void onFocus(ComponentEvent ce) {
		focused = true;

		super.onFocus(ce);
	}
}
