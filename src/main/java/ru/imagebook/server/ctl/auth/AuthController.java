package ru.imagebook.server.ctl.auth;

import ru.imagebook.server.service.auth.AuthService;
import ru.imagebook.server.service.auth.AuthSession;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.saasengine.client.ctl.auth.AuthMessages;
import ru.saasengine.client.ctl.auth.ConnectMessage;
import ru.saasengine.client.ctl.auth.DuplicateLoginMessage;
import ru.saasengine.client.ctl.auth.LoggedInMessage;
import ru.saasengine.client.ctl.auth.LoginBySIDMessage;
import ru.saasengine.client.ctl.auth.LoginFailedMessage;
import ru.saasengine.client.ctl.auth.LoginMessage;
import ru.saasengine.client.ctl.auth.ReconnectMessage;
import ru.saasengine.client.ctl.auth.SessionAspect;
import ru.saasengine.client.service.auth.AuthError;

public class AuthController extends Controller {
	private final AuthService service;

	public AuthController(Dispatcher dispatcher, final AuthService service) {
		super(dispatcher);

		this.service = service;
	}

	@Override
	public void registerHandlers() {
		addHandler(AuthMessages.LOGIN, new MessageHandler<LoginMessage>() {
			@Override
			public void handle(LoginMessage message) {
				try {
					AuthSession session = service.login(message.getCredentials());
					send(new LoggedInMessage(session.getId()));
				}
				catch (AuthError e) {
					send(new LoginFailedMessage());
				}
			}
		});

		addHandler(AuthMessages.LOGIN_BY_SID, new MessageHandler<LoginBySIDMessage>() {
			@Override
			public void handle(LoginBySIDMessage message) {
				try {
					AuthSession session = service.auth(message.getSessionId());
					send(new LoggedInMessage(session.getId()));
				}
				catch (AuthError e) {
					send(new LoginFailedMessage());
				}
			}
		});

		addHandler(AuthMessages.DUPLICATE_LOGIN, new MessageHandler<DuplicateLoginMessage>() {
			@Override
			public void handle(DuplicateLoginMessage message) {
				try {
					AuthSession session = service.duplicateSession(message.getSessionId());
					send(new LoggedInMessage(session.getId()));
				}
				catch (AuthError e) {
					send(new LoginFailedMessage());
				}
			}
		});

		addHandler(AuthMessages.CONNECT, new MessageHandler<ConnectMessage>() {
			@Override
			public void handle(ConnectMessage connectMessage) {
				try {
					Message message = service.connect(SessionAspect.getSessionId(connectMessage));
					if (message != null)
						send(message);
					send(new ReconnectMessage());
				}
				catch (AuthError e) {
					// TODO
				}
			}
		});
	}
}
