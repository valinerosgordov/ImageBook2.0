package ru.saasengine.client.ctl.failure;

import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.remoting.ErrorMessages;
import ru.minogin.core.client.flow.remoting.UnexpectedErrorMessage;

import com.google.inject.Inject;

public class FailureController extends Controller {
	private final FailureView view;

	@Inject
	public FailureController(Dispatcher dispatcher, final FailureView view) {
		super(dispatcher);
		
		this.view = view;
	}

	@Override
	public void registerHandlers() {
		addHandler(ErrorMessages.UNEXPECTED_ERROR, new MessageHandler<UnexpectedErrorMessage>() {
			@Override
			public void handle(UnexpectedErrorMessage message) {
				Throwable error = message.getError();
				if (error != null)
					error.printStackTrace();

				view.onUnknownError();
			}
		});

		addHandler(ErrorMessages.DISCONNECT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.onDisconnect();
			}
		});

		addHandler(ErrorMessages.INCOMPATIBLE_VERSION, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.onIncompatibleVersion();
			}
		});
	}
}
