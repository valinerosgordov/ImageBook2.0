package ru.imagebook.server.ctl.delivery;

import java.util.List;

import ru.imagebook.client.admin.ctl.delivery.AddOrderMessage;
import ru.imagebook.client.admin.ctl.delivery.DeliverMessage;
import ru.imagebook.client.admin.ctl.delivery.DeliveryMessages;
import ru.imagebook.client.admin.ctl.delivery.FindOrderMessage;
import ru.imagebook.client.admin.ctl.delivery.FindOrderResultMessage;
import ru.imagebook.client.admin.ctl.delivery.LoadOrdersResultMessage;
import ru.imagebook.client.admin.ctl.delivery.LoadTypeOrdersMessage;
import ru.imagebook.client.admin.ctl.delivery.LoadTypeOrdersResultMessage;
import ru.imagebook.client.admin.ctl.delivery.RemoveOrdersMessage;
import ru.imagebook.server.service.DeliveryService;
import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class DeliveryController extends Controller {
	private final DeliveryService service;

	public DeliveryController(Dispatcher dispatcher, DeliveryService service) {
		super(dispatcher);

		this.service = service;
	}

	@Override
	public void registerHandlers() {
		addHandler(DeliveryMessages.LOAD_ORDERS, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				List<Order<?>> orders = service.loadPrintedOrders();
				send(new LoadOrdersResultMessage(orders));
			}
		});

		addHandler(DeliveryMessages.LOAD_TYPE_ORDERS,
				new MessageHandler<LoadTypeOrdersMessage>() {
					@Override
					public void handle(LoadTypeOrdersMessage message) {
						Integer deliveryType = message.getDeliveryType();
						List<Order<?>> orders = service.loadDeliveryOrders(deliveryType);
						send(new LoadTypeOrdersResultMessage(deliveryType, orders));
					}
				});

		addHandler(DeliveryMessages.ADD_ORDER,
				new MessageHandler<AddOrderMessage>() {
					@Override
					public void handle(AddOrderMessage message) {
						String number = message.getNumber();
						boolean added = service.addOrder(number);

						if (added) {
							Message reply = new BaseMessage(DeliveryMessages.ADD_ORDER_RESULT);
							reply.addAspects(RemotingAspect.CLIENT);
							send(reply);
						}
					}
				});

		addHandler(DeliveryMessages.REMOVE_ORDERS,
				new MessageHandler<RemoveOrdersMessage>() {
					@Override
					public void handle(RemoveOrdersMessage message) {
						service.removeOrders(message.getOrderIds());

						Message reply = new BaseMessage(
								DeliveryMessages.REMOVE_ORDERS_RESULT);
						reply.addAspects(RemotingAspect.CLIENT);
						send(reply);
					}
				});

		addHandler(DeliveryMessages.FIND_ORDER,
				new MessageHandler<FindOrderMessage>() {
					@Override
					public void handle(FindOrderMessage message) {
						String number = message.getNumber();
						Order<?> order = service.findOrder(number);
						if (order != null)
							send(new FindOrderResultMessage(order));
					}
				});

		addHandler(DeliveryMessages.DELIVER, new MessageHandler<DeliverMessage>() {
			@Override
			public void handle(DeliverMessage message) {
				service.deliver(message.getOrderId(), message.getCode());

				Message reply = new BaseMessage(DeliveryMessages.DELIVER_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(ru.imagebook.server.ctl.delivery.DeliveryMessages.PRINT,
				new MessageHandler<PrintMessage>() {
					@Override
					public void handle(PrintMessage message) {
						service.print(message.getDeliveryType(), message.getWriter());
					}
				});
	}
}
