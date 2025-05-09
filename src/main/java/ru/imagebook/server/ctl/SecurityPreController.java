package ru.imagebook.server.ctl;

import ru.imagebook.server.service.SecurityService;
import ru.imagebook.server.service.UserService;
import ru.imagebook.shared.model.User;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class SecurityPreController extends Controller {
	private final SecurityService service;
	private final UserService userService;

	public SecurityPreController(Dispatcher dispatcher, final SecurityService service,
			UserService userService) {
		super(dispatcher);

		this.service = service;
		this.userService = userService;
	}

	@Override
	public void registerHandlers() {
		addPreHandler(new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				if (message.hasAspect(RemotingAspect.REMOTE) && message.hasAspect(SessionAspect.SESSION)) {
					int userId = RemoteSessionMessage.getUserId(message);
					User user = userService.getUserLite(userId);
					service.checkAccess(user, message);
					service.enableFilters(user);
				}
			}
		});
	}
}
