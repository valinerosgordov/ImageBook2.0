package ru.imagebook.client.common.ctl.register;

import ru.imagebook.client.common.util.YaMetrikaUtils;
import ru.imagebook.shared.model.Module;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.saasengine.client.ctl.auth.AuthMessage;
import ru.saasengine.client.ctl.auth.AuthMessages;
import ru.saasengine.client.model.auth.Credentials;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RegisterController extends Controller {
	private final RegisterView view;

	@Inject
	public RegisterController(Dispatcher dispatcher, RegisterView view) {
		super(dispatcher);

		this.view = view;
	}

	@Override
	public void registerHandlers() {
		addHandler(RegisterMessages.SHOW_REGISTER_FORM, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(AuthMessages.HIDE_DIALOG);

				view.showForm();
			}
		});

		addHandler(RegisterMessages.REGISTER_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.hideForm();
				view.infoRegisterResult();

				if (view.getModule() != null) {
					if (view.getModule() == Module.App) {
						YaMetrikaUtils.lkRegistractionGoal();
					} else if (view.getModule() == Module.Editor) {
						YaMetrikaUtils.editorRegistractionGoal();
					}
				}
			}
		});

		addHandler(RegisterMessages.REGISTER_INFORMED, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new AuthMessage(new Credentials()));
			}
		});

		addHandler(RegisterMessages.CANCEL, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.hideForm();

				send(new AuthMessage(new Credentials()));
			}
		});

		addHandler(RegisterMessages.USER_EXISTS, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.alertUserExists();
			}
		});

		addHandler(RegisterMessages.CAPTCHA_IS_INVALID, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.alertCaptchaIsInvalid();
			}
		});
	}
}
