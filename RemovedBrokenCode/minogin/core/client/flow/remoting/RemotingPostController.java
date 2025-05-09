package ru.minogin.core.client.flow.remoting;

import java.util.List;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.rpc.AbstractAsyncCallback;

import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RemotingPostController extends Controller {
	private static final int RESTORE_ON_FAILURE_DELAY_MS = 60 * 1000;

	private final RemotingServiceAsync service;

	@Inject
	public RemotingPostController(Dispatcher dispatcher, final RemotingServiceAsync service) {
		super(dispatcher);

		this.service = service;
	}

	@Override
	public void registerHandlers() {
		addPostHandler(new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				sendRemoteMessage(message);
			}
		});
	}

	private void sendRemoteMessage(final Message message) {
		if (message.hasAspect(RemotingAspect.REMOTE)) {
			service.send(message, new AbstractAsyncCallback<List<Message>>() {
				@Override
				public void onSuccess(List<Message> messages) {
					for (Message message : messages) {
						send(message);
					}
				}

				@Override
				public void onException(Exception e) {
					send(new UnexpectedErrorMessage(e));
				}

				@Override
				public void onDisconnect() {
					new Timer() {
						@Override
						public void run() {
							sendRemoteMessage(message);
						}
					}.schedule(RESTORE_ON_FAILURE_DELAY_MS);

					// send(new BaseMessage(ErrorMessages.DISCONNECT));
				}

				@Override
				public void onIncompatibleVersion() {
					send(new BaseMessage(ErrorMessages.INCOMPATIBLE_VERSION));
				}
			});
		}
	}
}
