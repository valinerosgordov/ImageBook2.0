package ru.imagebook.server.ctl;

import java.util.List;

import ru.imagebook.client.admin.ctl.request.*;
import ru.imagebook.server.service.request.RequestService;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Request;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.minogin.core.server.file.TempFile;
import ru.minogin.core.server.flow.download.RemoteDownloadMessage;

public class RequestController extends Controller {
	private final RequestService service;

	public RequestController(Dispatcher dispatcher, final RequestService service) {
		super(dispatcher);

		this.service = service;
	}

	@Override
	public void registerHandlers() {
		addHandler(RequestMessages.LOAD_REQUESTS, new MessageHandler<LoadRequestsMessage>() {
			@Override
			public void handle(LoadRequestsMessage message) {
				int offset = message.getOffset();
				List<Request> requests = service.loadRequests(offset, message.getLimit());
				long total = service.countRequests();

				send(new LoadRequestsResultMessage(requests, offset, total));
			}
		});

		addHandler(RequestMessages.LOAD_NON_BASKET_ORDERS,
				new MessageHandler<LoadNonBasketOrdersMessage>() {
					@Override
					public void handle(LoadNonBasketOrdersMessage message) {
						List<Order<?>> orders = service.loadNonBasketOrders();
						send(new LoadNonBasketOrdersResultMessage(orders));
					}
				});

		addHandler(RequestMessages.LOAD_BASKET_ORDERS, new MessageHandler<LoadBasketOrdersMessage>() {
			@Override
			public void handle(LoadBasketOrdersMessage message) {
				List<Order<?>> orders = service.loadBasketOrders();
				send(new LoadBasketOrdersResultMessage(orders));
			}
		});

		addHandler(RequestMessages.PUT_TO_BASKET, new MessageHandler<PutToBasketMessage>() {
			@Override
			public void handle(PutToBasketMessage message) {
				service.putToBasket(message.getOrderIds());

				Message reply = new BaseMessage(RequestMessages.PUT_TO_BASKET_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(RequestMessages.REMOVE_FROM_BASKET, new MessageHandler<RemoveFromBasketMessage>() {
			@Override
			public void handle(RemoveFromBasketMessage message) {
				service.removeFromBasket(message.getOrderIds());

				Message reply = new BaseMessage(RequestMessages.REMOVE_FROM_BASKET_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(RequestMessages.CREATE_REQUEST, new MessageHandler<CreateRequestMessage>() {
			@Override
			public void handle(CreateRequestMessage message) {
				service.createRequest(message.isUrgent());

				Message reply = new BaseMessage(RequestMessages.CREATE_REQUEST_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(RequestMessages.UPDATE_REQUEST, new MessageHandler<UpdateRequestMessage>() {
			@Override
			public void handle(UpdateRequestMessage message) {
				service.updateRequest(message.getRequest());

				Message reply = new BaseMessage(RequestMessages.UPDATE_REQUEST_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(RequestMessages.DELETE, new MessageHandler<DeleteRequestsMessage>() {
			@Override
			public void handle(DeleteRequestsMessage message) {
				service.deleteRequests(message.getRequestIds());

				Message reply = new BaseMessage(RequestMessages.DELETE_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});
		
		addHandler(RequestMessages.SEND_DAILY_REQUEST, new MessageHandler<SendDailyRequestMessage>() {
			@Override
			public void handle(SendDailyRequestMessage message) {
				service.generateAndSendRequests();
				
				Message reply = new BaseMessage(RequestMessages.TEST_REQUEST_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});
		
		addHandler(RequestMessages.SEND_WEEKLY_BOOK_REQUEST, new MessageHandler<SendWeeklyBookRequestMessage>() {
			@Override
			public void handle(SendWeeklyBookRequestMessage message) {
				service.sendWeeklyBookRequest();
				
				Message reply = new BaseMessage(RequestMessages.TEST_REQUEST_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});
		
        addHandler(RequestMessages.EXPORT_REQUEST_TO_XML_RESULT, new MessageHandler<ExportRequestToXmlMessage>() {
            @Override
            public void handle(ExportRequestToXmlMessage message) {
                service.exportLastRequestOrdersToXml();
                
                Message reply = new BaseMessage(RequestMessages.TEST_REQUEST_RESULT);
                reply.addAspects(RemotingAspect.CLIENT);
                send(reply);
            }
        });

		addHandler(RequestMessages.PRINT_REQUEST, new MessageHandler<PrintRequestMessage>() {
			@Override
			public void handle(PrintRequestMessage message) {
				TempFile file = service.printRequest(message.getRequestId());
				send(RemoteDownloadMessage.createMessage(file));
			}
		});

		addHandler(RequestMessages.BOOK_REQUEST, new MessageHandler<BookRequestMessage>() {
			@Override
			public void handle(BookRequestMessage message) {
				TempFile file = service.bookRequest(message.getRequestId());
				send(RemoteDownloadMessage.createMessage(file));
			}
		});
		
		addHandler(RequestMessages.URGENT_REQUEST, new MessageHandler<UrgentRequestMessage>() {
			@Override
			public void handle(UrgentRequestMessage message) {
				TempFile file = service.urgentRequest(message.getRequestId());
				send(RemoteDownloadMessage.createMessage(file));
			}
		});

		addHandler(RequestMessages.ACT, new MessageHandler<ActMessage>() {
			@Override
			public void handle(ActMessage message) {
				TempFile file = service.act(message.getRequestId());
				send(RemoteDownloadMessage.createMessage(file));
			}
		});

		addHandler(RequestMessages.DELIVERY, new MessageHandler<DeliveryMessage>() {
			@Override
			public void handle(DeliveryMessage message) {
				TempFile file = service.delivery(message.getRequestId());
				send(RemoteDownloadMessage.createMessage(file));
			}
		});

		addHandler(RequestMessages.CLOSE_REQUESTS, new MessageHandler<CloseRequestsMessage>() {
			@Override
			public void handle(CloseRequestsMessage message) {
				List<Integer> ids = message.getIds();
				service.closeRequests(ids);

				Message reply = new BaseMessage(RequestMessages.CLOSE_REQUESTS_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});
	}
}
