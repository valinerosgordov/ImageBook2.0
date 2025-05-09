package ru.imagebook.client.admin.ctl.request;

import java.util.ArrayList;
import java.util.List;

import ru.imagebook.shared.model.Request;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RequestController extends Controller {
	private List<Request> requestsToDelete;
	private final RequestView view;

	@Inject
	public RequestController(Dispatcher dispatcher, final RequestView view) {
		super(dispatcher);

		this.view = view;
	}

	@Override
	public void registerHandlers() {
		addHandler(RequestMessages.SHOW_SECTION, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.showSection();
			}
		});

		addHandler(RequestMessages.LOAD_REQUESTS_RESULT,
				new MessageHandler<LoadRequestsResultMessage>() {
					@Override
					public void handle(LoadRequestsResultMessage message) {
						List<Request> requests = message.getRequests();
						view.showRequests(requests, message.getOffset(), (int) message.getTotal());
					}
				});

		addHandler(RequestMessages.ADD_REQUEST, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.showAddForm();
				send(new LoadNonBasketOrdersMessage());
				send(new LoadBasketOrdersMessage());
			}
		});

		addHandler(RequestMessages.LOAD_NON_BASKET_ORDERS_RESULT,
				new MessageHandler<LoadNonBasketOrdersResultMessage>() {
					@Override
					public void handle(LoadNonBasketOrdersResultMessage message) {
						view.showNonBasketOrders(message.getOrders());
					}
				});

		addHandler(RequestMessages.LOAD_BASKET_ORDERS_RESULT,
				new MessageHandler<LoadBasketOrdersResultMessage>() {
					@Override
					public void handle(LoadBasketOrdersResultMessage message) {
						view.showBasketOrders(message.getOrders());
					}
				});

		addHandler(RequestMessages.PUT_TO_BASKET_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadNonBasketOrdersMessage());
				send(new LoadBasketOrdersMessage());
			}
		});

		addHandler(RequestMessages.REMOVE_FROM_BASKET_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadNonBasketOrdersMessage());
				send(new LoadBasketOrdersMessage());
			}
		});

		addHandler(RequestMessages.CREATE_REQUEST_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.closeAddForm();
				view.reloadGrid();
			}
		});

		addHandler(RequestMessages.UPDATE_REQUEST_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.closeEditForm();
				view.reloadGrid();
			}
		});

		addHandler(RequestMessages.REQUEST_SELECTED, new MessageHandler<RequestSelectedMessage>() {
			@Override
			public void handle(RequestSelectedMessage message) {
				Request request = message.getRequest();
				view.showRequest(request);
			}
		});

		addHandler(RequestMessages.DELETE_REQUEST, new MessageHandler<DeleteRequestRequestMessage>() {
			@Override
			public void handle(DeleteRequestRequestMessage message) {
				requestsToDelete = message.getRequests();
				if (!requestsToDelete.isEmpty())
					view.confirmDelete();
			}
		});

		addHandler(RequestMessages.DELETE_CONFIRMED, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				List<Integer> ids = new ArrayList<Integer>();
				for (Request request : requestsToDelete) {
					ids.add(request.getId());
				}
				send(new DeleteRequestsMessage(ids));
			}
		});

		addHandler(RequestMessages.DELETE_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.reloadGrid();
			}
		});

		addHandler(RequestMessages.CLOSE_REQUESTS_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.reloadGrid();
			}
		});

		addHandler(RequestMessages.EDIT_REQUEST, new MessageHandler<EditRequestMessage>() {
			@Override
			public void handle(EditRequestMessage message) {
				Request request = message.getRequest();
				view.showEditForm(request);
				send(new LoadNonBasketOrdersMessage());
			}
		});
		
        addHandler(RequestMessages.TEST_REQUEST_RESULT, new MessageHandler<Message>() {
            @Override
            public void handle(Message message) {
                view.notifyTestRequestDone();
            }
        });
	}
}
