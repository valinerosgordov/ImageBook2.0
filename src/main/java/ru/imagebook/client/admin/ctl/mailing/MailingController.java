package ru.imagebook.client.admin.ctl.mailing;

import ru.imagebook.client.common.service.I18nService;
import ru.imagebook.client.common.service.UserService;
import ru.imagebook.shared.model.Email;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class MailingController extends Controller {
	private final MailingView view;
	private final I18nService i18nService;
	private final UserService userService;

	@Inject
	public MailingController(Dispatcher dispatcher, MailingView view, I18nService i18nService,
			UserService userService) {
		super(dispatcher);

		this.view = view;
		this.i18nService = i18nService;
		this.userService = userService;
	}

	@Override
	public void registerHandlers() {
		addHandler(MailingMessages.SHOW_SECTION, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				Email email = userService.getUser().getEmails().iterator().next();
				view.showSection(i18nService.getLocale(), email.getEmail());

				send(new LoadMailingsMessage());
			}
		});

		addHandler(MailingMessages.LOAD_MAILINGS_RESULT,
				new MessageHandler<LoadMailingsResultMessage>() {
					@Override
					public void handle(LoadMailingsResultMessage message) {
						view.showMailings(message.getMailings());
					}
				});

		addHandler(MailingMessages.ADD_MAILING_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.hideAddForm();

				send(new LoadMailingsMessage());
			}
		});

		addHandler(MailingMessages.UPDATE_MAILING_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.hideEditForm();

				send(new LoadMailingsMessage());
			}
		});

		addHandler(MailingMessages.DELETE_MAILINGS_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadMailingsMessage());
			}
		});

		addHandler(MailingMessages.TEST_MAILING_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.hideTestForm();
			}
		});

		addHandler(MailingMessages.SEND_MAILING_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.enableSendButton();
				send(new LoadMailingsMessage());
			}
		});
	}
}
