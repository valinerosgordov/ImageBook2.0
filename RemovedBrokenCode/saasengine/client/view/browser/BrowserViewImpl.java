package ru.saasengine.client.view.browser;

import ru.minogin.core.client.browser.Browser;
import ru.minogin.core.client.browser.EscSuppresser;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.saasengine.client.ctl.browser.BrowserView;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class BrowserViewImpl extends View implements BrowserView {
	@Inject
	public BrowserViewImpl(Dispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void suppressEsc() {
		EscSuppresser.suppress();
	}

	@Override
	public void setCloseConfirmation(String message) {
		Browser.setCloseConfirmation(message);
	}
}
