package ru.imagebook.server.ctl.qiwi;

import ru.imagebook.server.ctl.RemoteSessionMessage;
import ru.imagebook.server.service.UserService;
import ru.imagebook.server.service.qiwi.QiwiService;
import ru.imagebook.shared.model.User;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.MessageHandler;

public class QiwiController extends Controller {
	private final QiwiService service;
	private final UserService userService;

	public QiwiController(Dispatcher dispatcher, QiwiService service, UserService userService) {
		super(dispatcher);

		this.service = service;
		this.userService = userService;
	}

	@Override
	public void registerHandlers() {
		addHandler(QiwiMessages.QIWI_PAY, new MessageHandler<QiwiPayMessage>() {
			@Override
			public void handle(QiwiPayMessage message) {
				int userId = RemoteSessionMessage.getUserId(message);
				User user = userService.getUser(userId);
				int billId = message.getBillId();
				String userName = message.getUserName();
				service.qiwiPay(user, billId, userName, message.getWriter());
			}
		});

		addHandler(QiwiMessages.QIWI_SUCCESS, new MessageHandler<QiwiSuccessMessage>() {
			@Override
			public void handle(QiwiSuccessMessage message) {
				service.qiwiSuccess(message.getWriter());
			}
		});

		addHandler(QiwiMessages.QIWI_FAIL, new MessageHandler<QiwiFailMessage>() {
			@Override
			public void handle(QiwiFailMessage message) {
				service.qiwiFail(message.getWriter());
			}
		});
	}
}
