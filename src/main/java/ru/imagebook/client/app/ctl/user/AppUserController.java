package ru.imagebook.client.app.ctl.user;

import ru.imagebook.client.common.ctl.user.UserController;
import ru.imagebook.client.common.ctl.user.UserMessages;
import ru.imagebook.client.common.ctl.user.UserView;
import ru.imagebook.client.common.service.UserService;
import ru.imagebook.shared.model.Module;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AppUserController extends UserController {
	@Inject
	public AppUserController(Dispatcher dispatcher, UserService userService, UserView view) {
		super(dispatcher, userService, view); 
	}
	
	@Override 
	public void registerHandlers() {
		super.registerHandlers();
		
		addHandler(UserMessages.SHOW_RECOVER_FORM, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				UserView userView = getUserView();
				userView.showRecoverForm(Module.App);
			}
		});
	}
}