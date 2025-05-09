package ru.imagebook.client.admin.ctl.delivery;

import ru.imagebook.client.common.service.I18nService;
import ru.imagebook.shared.model.DeliveryType;
import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.saasengine.client.service.auth.AuthService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DeliveryController extends Controller implements DeliveryPresenter {
	private static final int PERIOD_MS = 500;

	private static final int MODE_ADD = 1;
	private static final int MODE_DELIVERY = 2;

	private final DeliveryView view;
	private final AuthService authService;
	private final I18nService i18nService;

	private int mode;
	private Timer focusTimer;
	private Order<?> order;

	@Inject
	public DeliveryController(Dispatcher dispatcher, DeliveryView view,
			AuthService authService, I18nService i18nService) {
		super(dispatcher);

		this.view = view;
		view.setPresenter(this);
		this.authService = authService;
		this.i18nService = i18nService;
	}

	@Override
	public void registerHandlers() {
		addHandler(DeliveryMessages.SHOW_SECTION, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.showSection(authService.getSessionId());

				send(DeliveryMessages.SHOW_ORDERS);

				mode = MODE_ADD;
				focusTimer = new Timer() {
					@Override
					public void run() {
						if (mode == MODE_ADD)
							view.focusAddField();
						else
							view.focusDeliveryField();
					}
				};
				focusTimer.scheduleRepeating(PERIOD_MS);
			}
		});

		addHandler(DeliveryMessages.SHOW_ORDERS, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadOrdersMessage());
				for (int type : DeliveryType.values.keySet()) {
					send(new LoadTypeOrdersMessage(type));
				}
				send(new LoadTypeOrdersMessage(null));
			}
		});

		addHandler(DeliveryMessages.LOAD_ORDERS_RESULT,
				new MessageHandler<LoadOrdersResultMessage>() {
					@Override
					public void handle(LoadOrdersResultMessage message) {
						view.showOrders(message.getOrders(), i18nService.getLocale());
					}
				});

		addHandler(DeliveryMessages.LOAD_TYPE_ORDERS_RESULT,
				new MessageHandler<LoadTypeOrdersResultMessage>() {
					@Override
					public void handle(LoadTypeOrdersResultMessage message) {
						view.showTypeOrders(message.getDeliveryType(), message.getOrders(),
								i18nService.getLocale());
					}
				});

		addHandler(DeliveryMessages.ADD_ORDER_RESULT,
				new MessageHandler<Message>() {
					@Override
					public void handle(Message message) {
						send(DeliveryMessages.SHOW_ORDERS);
					}
				});

		addHandler(DeliveryMessages.REMOVE_ORDERS_RESULT,
				new MessageHandler<Message>() {
					@Override
					public void handle(Message message) {
						send(DeliveryMessages.SHOW_ORDERS);
					}
				});

		addHandler(DeliveryMessages.SECTION_CLOSED, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				focusTimer.cancel();
			}
		});

		addHandler(DeliveryMessages.TOGGLE_MODE, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				if (mode == MODE_ADD) {
					mode = MODE_DELIVERY;
					view.showDeliveryMode();
				}
				else {
					mode = MODE_ADD;
					view.showAddMode();
				}
			}
		});

		addHandler(DeliveryMessages.FIND_ORDER_RESULT,
				new MessageHandler<FindOrderResultMessage>() {
					@Override
					public void handle(FindOrderResultMessage message) {
						order = message.getOrder();
						view.showCodeForm(order);
					}
				});

		addHandler(DeliveryMessages.CANCEL_DELIVERY, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.showDeliveryMode();
			}
		});

		addHandler(DeliveryMessages.DELIVER, new MessageHandler<DeliverMessage>() {
			@Override
			public void handle(DeliverMessage message) {
				message.setOrderId(order.getId());
			}
		});

		addHandler(DeliveryMessages.DELIVER_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(DeliveryMessages.SHOW_ORDERS);
				view.showDeliveryMode();
			}
		});
	}

	@Override
	public void onCreateExcelButtonClick() {
		view.download(GWT.getHostPageBaseURL() + "posthouse.xls");
	}

	@Override
	public void onCreateBarcodesButtonClick() {
		view.download(GWT.getHostPageBaseURL() + "barcodes.doc");
	}
}
