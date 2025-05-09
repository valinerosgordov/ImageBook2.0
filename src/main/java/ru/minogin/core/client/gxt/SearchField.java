package ru.minogin.core.client.gxt;

import ru.minogin.core.client.text.StringUtil;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.TriggerField;
import com.google.gwt.user.client.Timer;

public class SearchField extends TriggerField<String> {
	private static final int DELAY_MS = 1000;

	private Timer timer;

	public SearchField(final SearchHandler handler) {
		setTriggerStyle("x-form-search-trigger");

		addListener(Events.KeyPress, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				if (timer != null)
					timer.cancel();

				timer = new Timer() {
					@Override
					public void run() {
						String value = getValue();
						value = StringUtil.trim(value);
						if (value != null && !value.isEmpty())
							updateTriggerStyle("x-form-clear-trigger");
						else
							updateTriggerStyle("x-form-search-trigger");

						handler.onSearch(value);
					}
				};
				timer.schedule(DELAY_MS);
			}
		});
		addListener(Events.TriggerClick, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				updateTriggerStyle("x-form-search-trigger");
				reset();
				handler.onSearch(null);
			}
		});
	}

	public void updateTriggerStyle(String triggerStyle) {
		trigger.setStyleName("x-form-trigger " + triggerStyle);
	}
}
