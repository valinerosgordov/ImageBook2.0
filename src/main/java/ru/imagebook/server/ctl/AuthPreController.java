package ru.imagebook.server.ctl;

import java.util.ArrayList;
import java.util.List;

import ru.imagebook.client.calc.ctl.CalcMessages;
import ru.imagebook.client.common.ctl.register.RegisterMessages;
import ru.imagebook.client.common.ctl.user.UserMessages;
import ru.imagebook.server.ctl.action.ActionMessages;
import ru.imagebook.server.ctl.flash.FlashMessages;
import ru.imagebook.server.ctl.qiwi.QiwiMessages;
import ru.imagebook.server.ctl.site.SiteMessages;
import ru.imagebook.server.service.SessionHolder;
import ru.minogin.core.client.common.AccessDeniedError;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.AuthMessages;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class AuthPreController extends Controller {
	private final List<String> nonSessionMessages = new ArrayList<String>();
	private final SessionHolder sessionHolder;

	public AuthPreController(Dispatcher dispatcher, final SessionHolder sessionHolder) {
		super(dispatcher);

		this.sessionHolder = sessionHolder;
	}

	@Override
	public void registerHandlers() {
		nonSessionMessages.add(ActionMessages.ACTIVATE_REQUEST);
		nonSessionMessages.add(AuthMessages.LOGIN);
		nonSessionMessages.add(AuthMessages.LOGIN_BY_SID);
		nonSessionMessages.add(AuthMessages.DUPLICATE_LOGIN);
		nonSessionMessages.add(CalcMessages.LOAD_DATA);
		nonSessionMessages.add(QiwiMessages.QIWI_SUCCESS);
		nonSessionMessages.add(QiwiMessages.QIWI_FAIL);
		nonSessionMessages.add(UserMessages.RECOVER_PASSWORD);
		nonSessionMessages.add(ru.imagebook.server.ctl.user.UserMessages.RECOVER_PASSWORD);
		nonSessionMessages.add(SiteMessages.SHOW_PAGE);
		nonSessionMessages.add(FlashMessages.SHOW_FLASH_XML);
		nonSessionMessages.add(FlashMessages.SHOW_FLASH_IMAGE);
		nonSessionMessages.add(FlashMessages.SHOW_WEB_FLASH_XML);
		nonSessionMessages.add(FlashMessages.SHOW_WEB_FLASH_IMAGE);
		nonSessionMessages.add(RegisterMessages.REGISTER);

		addPreHandler(new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				if (message.hasAspect(RemotingAspect.REMOTE)) {
					if (!message.hasAspect(SessionAspect.SESSION)
							&& !nonSessionMessages.contains(message.getMessageType()))
						throw new AccessDeniedError();
				}
			}
		});

		addPreHandler(new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				if (message.hasAspect(SessionAspect.SESSION)) {
					String sessionId = SessionAspect.getSessionId(message);
					sessionHolder.acquireSessionForRequest(sessionId);

					RemoteSessionMessage.setUserId(message, sessionHolder.getUser().getId());
					RemoteSessionMessage.setAccount(message, sessionHolder.getAccount());
				}
			}
		});
	}
}
