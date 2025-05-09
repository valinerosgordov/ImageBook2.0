package ru.saasengine.client.ctl.auth;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.saasengine.client.service.auth.AuthService;

import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AuthController extends Controller {
	public static final int CONNECT_DELAY_MS = 1000;

	private boolean connected;

	private final AuthView view;

	private final AuthService authService;

	@Inject
	public AuthController(Dispatcher dispatcher, final AuthView view, final AuthService authService) {
		super(dispatcher);

		this.view = view;
		this.authService = authService;
	}

	@Override
	public void registerHandlers() {
		addHandler(AuthMessages.AUTH, new MessageHandler<AuthMessage>() {
			@Override
			public void handle(AuthMessage message) {
				view.showLoginDialog(message.getCredentials());
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

		addHandler(AuthMessages.LOGIN_FAILED, new MessageHandler<LoginFailedMessage>() {
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
