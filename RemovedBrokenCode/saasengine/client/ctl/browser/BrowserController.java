package ru.saasengine.client.ctl.browser;

import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.MessageHandler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class BrowserController extends Controller {
	private final BrowserView view;

	@Inject
	public BrowserController(Dispatcher dispatcher, final BrowserView view) {
		super(dispatcher);

		this.view = view;
	}

	@Override
	public void registerHandlers() {
		addHandler(BrowserMessages.PREPARE_BROWSER, new MessageHandler<PrepareBrowserMessage>() {
			@Override
			public void handle(PrepareBrowserMessage message) {
				view.suppressEsc();
				view.setCloseConfirmation(message.getAppName());
			}
		});
	}
}
