package ru.imagebook.server.ctl;

import ru.imagebook.client.admin.ctl.AdminMessages;
import ru.imagebook.server.service.UpdateService;
import ru.imagebook.server.service.backup.BackupService;
import ru.imagebook.server.service.clean.CleanService;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class AdminController extends Controller {
	private final BackupService service;
	private final UpdateService updateService;
	private final CleanService cleanService;

	public AdminController(Dispatcher dispatcher, final BackupService service,
			final UpdateService updateService, CleanService cleanService) {
		super(dispatcher);

		this.service = service;
		this.updateService = updateService;
		this.cleanService = cleanService;
	}

	@Override
	public void registerHandlers() {
		addHandler(AdminMessages.BACKUP, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				service.backup();

				Message reply = new BaseMessage(AdminMessages.BACKUP_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(AdminMessages.CLEAN, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				cleanService.clean();

				Message reply = new BaseMessage(AdminMessages.CLEAN_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(AdminMessages.UPDATE, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				updateService.update();

				Message reply = new BaseMessage(AdminMessages.UPDATE_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});
	}
}
