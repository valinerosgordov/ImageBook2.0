package ru.imagebook.client.editor.ctl.user;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.common.ctl.user.UserController;
import ru.imagebook.client.common.ctl.user.UserMessages;
import ru.imagebook.client.common.ctl.user.UserView;
import ru.imagebook.client.common.service.UserService;
import ru.imagebook.shared.model.Module;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;

@Singleton
public class EditorUserController extends UserController {
	@Inject
	public EditorUserController(Dispatcher dispatcher, UserService userService,
			UserView view) {
		super(dispatcher, userService, view); 
	}
	
	@Override 
	public void registerHandlers() {
		super.registerHandlers();
		
		addHandler(UserMessages.SHOW_RECOVER_FORM, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				UserView userView = getUserView();
				userView.showRecoverForm(Module.Editor);
			}
		});
	}
}
