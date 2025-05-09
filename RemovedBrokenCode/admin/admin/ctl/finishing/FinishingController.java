package ru.imagebook.client.admin.ctl.finishing;

import java.util.Date;
import java.util.List;

import ru.imagebook.client.common.service.I18nService;
import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.saasengine.client.service.auth.AuthService;

import com.extjs.gxt.ui.client.util.DateWrapper;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class FinishingController extends Controller {
	private static final int FOCUS_PERIOD_MS = 500;
	public static final String STYLE = "style";

	private final FinishingView view;
	private final I18nService i18nService;
	private final AuthService authService;

	private Timer timer;
	private List<Order<?>> orders;

	@Inject
	public FinishingController(Dispatcher dispatcher, FinishingView view, I18nService i18nService,
			AuthService authService) {
		super(dispatcher);

		this.view = view;
		this.i18nService = i18nService;
		this.authService = authService;
	}

	@Override
	public void registerHandlers() {
		addHandler(FinishingMessages.SHOW_SECTION, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.showSection();

				timer = new Timer() {
					@Override
					public void run() {
						view.focusNumberField();
					}
				};
				timer.scheduleRepeating(FOCUS_PERIOD_MS);

				send(FinishingMessages.SHOW_ORDERS);
			}
		});

		addHandler(FinishingMessages.SECTION_CLOSED, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				orders = null;
				timer.cancel();
			}
		});

		addHandler(FinishingMessages.SHOW_ORDERS, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadOrdersMessage());
			}
		});

		addHandler(FinishingMessages.LOAD_ORDERS_RESULT, new MessageHandler<LoadOrdersResultMessage>() {
			@Override
			public void handle(LoadOrdersResultMessage message) {
				DateWrapper dateWrapper = new DateWrapper(new Date());
				dateWrapper = dateWrapper.clearTime();
				dateWrapper = dateWrapper.addDays(-1);
				Date date1 = dateWrapper.asDate();

				dateWrapper = new DateWrapper(new Date());
				dateWrapper = dateWrapper.clearTime();
				dateWrapper = dateWrapper.addDays(1);
				Date date2 = dateWrapper.asDate();

				orders = message.getOrders();
				for (Order<?> order : orders) {
					Date orderDate = order.getPrintDate();
					if (orderDate != null) {
						if (orderDate.before(date1))
							order.set(STYLE, "red-row");
						else if (orderDate.before(date2))
							order.set(STYLE, "green-row");
					}
				}
				view.showOrders(orders, i18nService.getLocale());
			}
		});

		addHandler(FinishingMessages.SHOW_ORDER, new MessageHandler<ShowOrderMessage>() {
			@Override
			public void handle(ShowOrderMessage message) {
				String number = message.getNumber();
				Order<?> order = null;
				for (Order<?> o : orders) {
					if (o.getNumber().equalsIgnoreCase(number)) {
						order = o;
					}
				}

				view.resetNumberField();

				if (order != null) {
					if (message.isScanned())
						send(new OrderScannedMessage(order.getId()));

					view.showOrder(order, i18nService.getLocale(), authService.getSessionId());
				}
				else
					view.resetOrder();
			}
		});

		addHandler(FinishingMessages.FINISH_ORDER_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(FinishingMessages.SHOW_ORDERS);

				view.resetOrder();
			}
		});
	}
}
