package ru.imagebook.client.admin.ctl;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.saasengine.client.ctl.desktop.DesktopMessages;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DesktopController extends Controller {
	private final DesktopView view;

	@Inject
	public DesktopController(Dispatcher dispatcher, final DesktopView view) {
		super(dispatcher);

		this.view = view;
	}

	@Override
	public void registerHandlers() {
		addHandler(DesktopMessages.SHOW_DESKTOP, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.showDesktop();

				send(new BaseMessage(DesktopMessages.DESKTOP_SHOWN));
			}
		});
	}
}
