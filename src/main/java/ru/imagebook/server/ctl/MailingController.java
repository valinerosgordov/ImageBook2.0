package ru.imagebook.server.ctl;

import java.util.List;

import ru.imagebook.client.admin.ctl.mailing.AddMailingMessage;
import ru.imagebook.client.admin.ctl.mailing.DeleteMailingsMessage;
import ru.imagebook.client.admin.ctl.mailing.LoadMailingsMessage;
import ru.imagebook.client.admin.ctl.mailing.LoadMailingsResultMessage;
import ru.imagebook.client.admin.ctl.mailing.MailingMessages;
import ru.imagebook.client.admin.ctl.mailing.SendMailingMessage;
import ru.imagebook.client.admin.ctl.mailing.TestMailingMessage;
import ru.imagebook.client.admin.ctl.mailing.UpdateMailingMessage;
import ru.imagebook.server.service.MailingService;
import ru.imagebook.shared.model.Mailing;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.remoting.ReplyMessage;

public class MailingController extends Controller {
	private final MailingService service;

	public MailingController(Dispatcher dispatcher, MailingService service) {
		super(dispatcher);

		this.service = service;
	}

	@Override
	public void registerHandlers() {
		addHandler(MailingMessages.LOAD_MAILINGS, new MessageHandler<LoadMailingsMessage>() {
			@Override
			public void handle(LoadMailingsMessage message) {
				List<Mailing> mailings = service.loadMailings();
				send(new LoadMailingsResultMessage(mailings));
			}
		});

		addHandler(MailingMessages.ADD_MAILING, new MessageHandler<AddMailingMessage>() {
			@Override
			public void handle(AddMailingMessage message) {
				service.addMailing(message.getMailing());

				send(new ReplyMessage(MailingMessages.ADD_MAILING_RESULT));
			}
		});

		addHandler(MailingMessages.UPDATE_MAILING, new MessageHandler<UpdateMailingMessage>() {
			@Override
			public void handle(UpdateMailingMessage message) {
				service.updateMailing(message.getMailing());

				send(new ReplyMessage(MailingMessages.UPDATE_MAILING_RESULT));
			}
		});

		addHandler(MailingMessages.DELETE_MAILINGS, new MessageHandler<DeleteMailingsMessage>() {
			@Override
			public void handle(DeleteMailingsMessage message) {
				service.deleteMailings(message.getIds());

				send(new ReplyMessage(MailingMessages.DELETE_MAILINGS_RESULT));
			}
		});

		addHandler(MailingMessages.TEST_MAILING, new MessageHandler<TestMailingMessage>() {
			@Override
			public void handle(TestMailingMessage message) {
				int userId = RemoteSessionMessage.getUserId(message);
				service.testMailing(message.getId(), userId, message.getEmail());

				send(new ReplyMessage(MailingMessages.TEST_MAILING_RESULT));
			}
		});

		addHandler(MailingMessages.SEND_MAILING, new MessageHandler<SendMailingMessage>() {
			@Override
			public void handle(SendMailingMessage message) {
				service.sendMailing(message.getId());

				send(new ReplyMessage(MailingMessages.SEND_MAILING_RESULT));
			}
		});
	}
}
