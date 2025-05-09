package ru.saasengine.client.ctl.auth;

import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.saasengine.client.service.auth.AuthService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AuthPreController extends Controller {
	private final AuthService authService;

	@Inject
	public AuthPreController(Dispatcher dispatcher, final AuthService authService) {
		super(dispatcher);

		this.authService = authService;
	}

	@Override
	public void registerHandlers() {
		addPreHandler(new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				if (message.hasAspect(SessionAspect.SESSION)) {
					if (authService.isSessionStarted()) {
						String sessionId = authService.getSessionId();
						SessionAspect.setSessionId(message, sessionId);
					}
				}
			}
		});
	}
}
