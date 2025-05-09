package ru.saasengine.client.view.failure;

import java.util.Date;

import ru.minogin.core.client.browser.Browser;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.saasengine.client.ctl.failure.FailureView;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class FailureViewImpl extends View implements FailureView {
	private final FailureConstants constants;
	private final FailureMessages messages;

	@Inject
	public FailureViewImpl(Dispatcher dispatcher, FailureConstants constants, FailureMessages messages) {
		super(dispatcher);

		this.constants = constants;
		this.messages = messages;
	}

	@Override
	public void onUnknownError() {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				MessageBox.alert(constants.error(), messages.unexpectedError(new Date()), null);
			}
		});
	}

	@Override
	public void onDisconnect() {
		MessageBox.alert(constants.error(), constants.disconnect(), null);
	}

	@Override
	public void onIncompatibleVersion() {
		MessageBox.alert(constants.error(), constants.incompatibleVersion(),
				new Listener<MessageBoxEvent>() {
					@Override
					public void handleEvent(MessageBoxEvent be) {
						Browser.refresh();
					}
				});
	}
}
