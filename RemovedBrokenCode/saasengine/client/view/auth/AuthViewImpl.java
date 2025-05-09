package ru.saasengine.client.view.auth;

import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.saasengine.client.ctl.auth.AuthView;
import ru.saasengine.client.ctl.auth.LoginMessage;
import ru.saasengine.client.model.auth.Credentials;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AuthViewImpl extends View implements AuthView {
	private final AuthConstants constants;
	private final AuthBundle bundle;
	private final CommonConstants appConstants;

	private LoginDialog dialog;

	@Inject
	public AuthViewImpl(Dispatcher dispatcher, AuthConstants constants, AuthBundle bundle,
			CommonConstants appConstants) {
		super(dispatcher);

		this.constants = constants;
		this.bundle = bundle;
		this.appConstants = appConstants;
	}

	@Override
	public void showLoginDialog(final Credentials credentials) {
		dialog = new LoginDialog(credentials, constants, bundle) {
			@Override
			protected void onLogin() {
				AuthViewImpl.this.onLogin(credentials);
			}
		};
		dialog.show();
	}

	protected void onLogin(Credentials credentials) {
		send(new LoginMessage(credentials));
	}

	protected boolean notEmpty(TextField<String> field) {
		return field.getValue() != null && field.getValue().length() > 0;
	}

	@Override
	public void loginFailed() {
		MessageBox.alert(appConstants.error(), constants.authFailed(), new Listener<MessageBoxEvent>() {
			@Override
			public void handleEvent(MessageBoxEvent be) {
				if (dialog != null)
					dialog.reset();
			}
		});
	}

	@Override
	public void hideLoginDialog() {
		if (dialog != null)
			dialog.hide();
	}

	@Override
	public void concurrentLogin() {
		MessageBox box = new MessageBox();
		box.setIcon(MessageBox.WARNING);
		box.setTitle(appConstants.warning());
		box.setMessage(constants.concurrentLogin());
		box.setButtons("");
		box.show();
	}
}
