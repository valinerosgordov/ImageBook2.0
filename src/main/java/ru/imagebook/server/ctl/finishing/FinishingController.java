package ru.imagebook.server.ctl.finishing;

import java.util.List;

import ru.imagebook.client.admin.ctl.finishing.FinishOrderMessage;
import ru.imagebook.client.admin.ctl.finishing.FinishingMessages;
import ru.imagebook.client.admin.ctl.finishing.LoadOrdersMessage;
import ru.imagebook.client.admin.ctl.finishing.LoadOrdersResultMessage;
import ru.imagebook.client.admin.ctl.finishing.OrderScannedMessage;
import ru.imagebook.server.service.FinishingService;
import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class FinishingController extends Controller {
	private final FinishingService service;

	public FinishingController(Dispatcher dispatcher, FinishingService service) {
		super(dispatcher);

		this.service = service;
	}

	@Override
	public void registerHandlers() {
		addHandler(FinishingMessages.LOAD_ORDERS, new MessageHandler<LoadOrdersMessage>() {
			@Override
			public void handle(LoadOrdersMessage message) {
				List<Order<?>> orders = service.loadOrders();
				send(new LoadOrdersResultMessage(orders));
			}
		});

		addHandler(FinishingMessages.ORDER_SCANNED, new MessageHandler<OrderScannedMessage>() {
			@Override
			public void handle(OrderScannedMessage message) {
				service.scan(message.getOrderId());
			}
		});

		addHandler(FinishingMessages.FINISH_ORDER, new MessageHandler<FinishOrderMessage>() {
			@Override
			public void handle(FinishOrderMessage message) {
				service.finishOrder(message.getOrderId());

				BaseMessage reply = new BaseMessage(FinishingMessages.FINISH_ORDER_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(RemoteFinishingMessages.SHOW_PREVIEW, new MessageHandler<ShowPreviewMessage>() {
			@Override
			public void handle(ShowPreviewMessage message) {
				service.showPreview(message.getOrderId(), message.getStream());
			}
		});
	}
}
