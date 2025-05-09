package ru.imagebook.client.common.ctl.user;

import ru.imagebook.client.common.service.UserService;
import ru.imagebook.shared.model.User;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class UserController extends Controller {
	private final UserService userService;
	private final UserView view;

	@Inject
	public UserController(Dispatcher dispatcher, final UserService userService,
			UserView view) {
		super(dispatcher);

		this.userService = userService;
		this.view = view;
	}

	@Override
	public void registerHandlers() {
		addHandler(UserMessages.USER_LOADED,
				new MessageHandler<UserLoadedMessage>() {
					@Override
					public void handle(UserLoadedMessage message) {
						User user = message.getUser();
						userService.setUser(user);

						send(UserMessages.USER_ACQUIRED);
					}
				});

		addHandler(UserMessages.RECOVER_PASSWORD_RESULT,
				new MessageHandler<Message>() {
					@Override
					public void handle(Message message) {
						view.hideRecoverForm();
						view.infoRecoverMailSent();
					}
				});
	}

	protected UserView getUserView() {
		return view;
	}	
}
