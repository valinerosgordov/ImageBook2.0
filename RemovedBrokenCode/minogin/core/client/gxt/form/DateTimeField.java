package ru.minogin.core.client.gxt.form;

import java.util.Date;


import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.DateWrapper;
import com.extjs.gxt.ui.client.widget.form.MultiField;
import com.extjs.gxt.ui.client.widget.form.Time;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;

public class DateTimeField extends MultiField<Date> {
	private XDateField dateField;
	private XTimeField timeField;

	public DateTimeField() {
		dateField = new XDateField();
		timeField = new XTimeField();
		add(dateField);
		add(timeField);
		setSpacing(5);
		setResizeFields(true);

		dateField.addListener(Events.Blur, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				onBlur();
			}
		});
		timeField.addListener(Events.Blur, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				onBlur();
			}
		});
	}

	private void onBlur() {
		Scheduler scheduler = Scheduler.get();
		scheduler.scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				if (!dateField.isFocused() && !timeField.isFocused())
					fireEvent(Events.Blur);
			}
		});
	}

	@Override
	public Date getValue() {
		Date date = dateField.getValue();
		if (date != null) {
			DateWrapper dateWrapper = new DateWrapper(date);
			dateWrapper.clearTime();
			Time time = timeField.getValue();
			if (time != null) {
				dateWrapper = dateWrapper.addHours(time.getHour());
				dateWrapper = dateWrapper.addMinutes(time.getMinutes());
			}
			Date newDate = dateWrapper.asDate();
			return newDate;
		}
		else
			return null;
	}

	@Override
	public void setValue(Date value) {
		super.setValue(value);

		dateField.setValue(value);
		timeField.setDateValue(value);
	}
}
