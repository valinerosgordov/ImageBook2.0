package ru.imagebook.server.ctl;

import ru.imagebook.client.common.ctl.register.RegisterMessage;
import ru.imagebook.client.common.ctl.register.RegisterMessages;
import ru.imagebook.server.service.RegisterService;
import ru.imagebook.shared.service.app.UserCaptchaIsInvalid;
import ru.imagebook.shared.service.app.UserExistsException;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.minogin.core.server.flow.remoting.MessageError;

public class RegisterController extends Controller {
	private final RegisterService service;

	public RegisterController(Dispatcher dispatcher, RegisterService service) {
		super(dispatcher);
		this.service = service;
	}

	@Override
	public void registerHandlers() {
		addHandler(RegisterMessages.REGISTER, new MessageHandler<RegisterMessage>() {
            @Override
            public void handle(RegisterMessage message) {
                try {
                    service.register(message.getName(), message.getEmail(), message.getCaptcha(), message.getModule());
                } catch (UserExistsException e) {
                    throw new MessageError(RegisterMessages.USER_EXISTS);
                } catch (UserCaptchaIsInvalid e) {
                    throw new MessageError(RegisterMessages.CAPTCHA_IS_INVALID);
                }

                Message reply = new BaseMessage(RegisterMessages.REGISTER_RESULT);
                reply.addAspects(RemotingAspect.CLIENT);
                send(reply);
            }
        });
	}
}
