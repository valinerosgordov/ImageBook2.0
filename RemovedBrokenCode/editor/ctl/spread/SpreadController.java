package ru.imagebook.client.editor.ctl.spread;

import ru.imagebook.client.editor.ctl.order.OrderMessages;
import ru.imagebook.shared.model.AlbumOrder;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.saasengine.client.service.auth.AuthService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SpreadController extends Controller {
	private final SpreadView view;
	private final AuthService authService;

	@Inject
	public SpreadController(Dispatcher dispatcher, SpreadView view, AuthService authService) {
		super(dispatcher);

		this.view = view;
		this.authService = authService;
	}

	@Override
	public void registerHandlers() {
		addHandler(SpreadMessages.SHOW_SPREAD_PAGE, new MessageHandler<ShowSpreadPageMessage>() {
			@Override
			public void handle(ShowSpreadPageMessage message) {
				AlbumOrder order = message.getOrder();
				int pageNumber = message.getPageNumber();
				view.showSpread(order, pageNumber, authService.getSessionId());
			}
		});
		
		addHandler(OrderMessages.CLOSE_ORDER, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.hideSpread();
			}
		});
	}
}
