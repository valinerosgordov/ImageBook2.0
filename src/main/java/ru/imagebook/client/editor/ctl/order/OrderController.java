package ru.imagebook.client.editor.ctl.order;

import java.util.List;
import java.util.Map;

import ru.imagebook.client.common.service.I18nService;
import ru.imagebook.client.editor.ctl.file.AddImageMessage;
import ru.imagebook.client.editor.ctl.file.FileMessages;
import ru.imagebook.client.editor.ctl.pages.ShowPageMessage;
import ru.imagebook.client.editor.ctl.spread.ShowSpreadPageMessage;
import ru.imagebook.client.editor.service.EditorService;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.editor.Page;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class OrderController extends Controller {
	private final OrderView view;
	private final I18nService i18nService;
	private final EditorService service;

	@Inject
	public OrderController(Dispatcher dispatcher, OrderView view, I18nService i18nService,
			EditorService service) {
		super(dispatcher);

		this.view = view;
		this.i18nService = i18nService;
		this.service = service;
	}

	@Override
	public void registerHandlers() {
		addHandler(OrderMessages.NEW_ORDER, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadProductsMessage());
			}
		});

		addHandler(OrderMessages.LOAD_PRODUCTS_RESULT, new MessageHandler<LoadProductsResultMessage>() {
			@Override
			public void handle(LoadProductsResultMessage message) {
				Map<Integer, List<Product>> products = message.getProducts();
				List<Order<?>> orders = message.getOrders();

				view.showOrderForm(products, i18nService.getLocale(), orders);
			}
		});

		addHandler(OrderMessages.CREATE_ORDER_RESULT, new MessageHandler<CreateOrderResultMessage>() {
			@Override
			public void handle(CreateOrderResultMessage message) {
				AlbumOrder order = (AlbumOrder) message.getOrder();
				service.setOrder(order);
				view.hideCreateOrderForm();

				send(new ShowOrderMessage(order));
				send(FileMessages.SHOW_FOLDERS);

				send(OrderMessages.ORDER_SHOWN);
			}
		});

		addHandler(OrderMessages.SHOW_ORDER, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				int currentPage = 0;
				service.setPageNumber(currentPage);
				service.setVariant(0);
				send(new ShowSpreadPageMessage(service.getOrder(), currentPage));
			}
		});

		addHandler(OrderMessages.OPEN_ORDER_REQUEST, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadOrdersMessage());
			}
		});

		addHandler(OrderMessages.LOAD_ORDERS_RESULT, new MessageHandler<LoadOrdersResultMessage>() {
			@Override
			public void handle(LoadOrdersResultMessage message) {
				view.showOpenOrderForm(message.getOrders(), i18nService.getLocale());
			}
		});

		addHandler(OrderMessages.OPEN_ORDER_RESULT, new MessageHandler<OpenOrderResultMessage>() {
			@Override
			public void handle(OpenOrderResultMessage message) {
				AlbumOrder order = (AlbumOrder) message.getOrder();
				service.setOrder(order);
				view.hideOpenOrderForm();

				send(new ShowOrderMessage(order));
				send(FileMessages.SHOW_FOLDERS);

				send(OrderMessages.ORDER_SHOWN);
			}
		});

		addHandler(FileMessages.ADD_IMAGE, new MessageHandler<AddImageMessage>() {
			@Override
			public void handle(AddImageMessage message) {
				int currentPage = service.getPageNumber();
				message.setPageNumber(currentPage);
				message.setVariant(service.getVariant());
			}
		});

		addHandler(OrderMessages.SHOW_EDITOR_COMMON_MESSAGE, new MessageHandler<ShowEditorCommonErrorMessage>() {
			@Override
			public void handle(ShowEditorCommonErrorMessage message) {
				view.showEditorCommonMessage(message.getErrorMessage());
			}
		});

		addHandler(OrderMessages.PAGE_CHANGED, new MessageHandler<PageChangedMessage>() {
			@Override
			public void handle(PageChangedMessage message) {
				int currentPage = service.getPageNumber();
				AlbumOrder order = service.getOrder();

				int number = message.getNumber();
				Page page = message.getPage();
				order.getLayout().getPages().set(number, page);

				if (number == currentPage)
					send(new ShowSpreadPageMessage(order, currentPage));

				send(new ShowPageMessage(order, number));
			}
		});

		addHandler(OrderMessages.PROCESS_ORDER_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new CloseOrderMessage());

				view.infoOrderProcessed();
			}
		});

		addHandler(OrderMessages.CLOSE_ORDER, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				service.setOrder(null);
			}
		});
	}
}
