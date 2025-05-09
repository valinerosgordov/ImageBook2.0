package ru.imagebook.client.common.ctl.auth;

import ru.imagebook.client.common.service.vendor.VendorService;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.saasengine.client.ctl.auth.AuthMessage;
import ru.saasengine.client.ctl.auth.AuthMessages;
import ru.saasengine.client.ctl.auth.ConnectMessage;
import ru.saasengine.client.ctl.auth.LoggedInMessage;
import ru.saasengine.client.ctl.auth.LoginFailedMessage;
import ru.saasengine.client.ctl.auth.ReconnectMessage;
import ru.saasengine.client.ctl.auth.SessionAspect;
import ru.saasengine.client.service.auth.AuthService;

import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AuthController extends Controller {
	public static final int CONNECT_DELAY_MS = 1000;

	private final AuthView view;
	private final AuthService authService;
	private final VendorService vendorService;

	private boolean connected;

	@Inject
	public AuthController(Dispatcher dispatcher, final AuthView view,
			final AuthService authService, VendorService vendorService) {
		super(dispatcher);

		this.view = view;
		this.authService = authService;
		this.vendorService = vendorService;
	}

	@Override
	public void registerHandlers() {
		addHandler(AuthMessages.AUTH, new MessageHandler<AuthMessage>() {
			@Override
			public void handle(AuthMessage message) {
				Vendor vendor = vendorService.getVendor();
				view.showLoginDialog(message.getCredentials(), vendor);
			}
		});

		addHandler(AuthMessages.LOGGED_IN, new MessageHandler<LoggedInMessage>() {
			@Override
			public void handle(LoggedInMessage message) {
				connected = true;

				view.hideLoginDialog();

				authService.setSessionId(SessionAspect.getSessionId(message));

				new Timer() {
					@Override
					public void run() {
						send(new ConnectMessage());
					}
				}.schedule(CONNECT_DELAY_MS);

				send(new BaseMessage(AuthMessages.SESSION_STARTED));
			}
		});

		addHandler(AuthMessages.RECONNECT, new MessageHandler<ReconnectMessage>() {
			@Override
			public void handle(ReconnectMessage message) {
				if (connected)
					send(new ConnectMessage());
			}
		});

		addHandler(AuthMessages.LOGIN_FAILED,
				new MessageHandler<LoginFailedMessage>() {
					@Override
					public void handle(LoginFailedMessage message) {
						view.loginFailed();
					}
				});

		addHandler(AuthMessages.CONCURRENT_LOGIN, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				connected = false;
				view.concurrentLogin();
			}
		});

		addHandler(AuthMessages.HIDE_DIALOG, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.hideLoginDialog();
			}
		});
	}
}
