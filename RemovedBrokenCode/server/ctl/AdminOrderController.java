package ru.imagebook.server.ctl;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;

import ru.imagebook.client.admin.ctl.order.AddOrderMessage;
import ru.imagebook.client.admin.ctl.order.ApplyFilterMessage;
import ru.imagebook.client.admin.ctl.order.DataLoadedMessage;
import ru.imagebook.client.admin.ctl.order.DeleteOrdersMessage;
import ru.imagebook.client.admin.ctl.order.GeneratePdfMessage;
import ru.imagebook.client.admin.ctl.order.LoadOrdersMessage;
import ru.imagebook.client.admin.ctl.order.LoadOrdersResultMessage;
import ru.imagebook.client.admin.ctl.order.LoadUsersMessage;
import ru.imagebook.client.admin.ctl.order.LoadUsersResultMessage;
import ru.imagebook.client.admin.ctl.order.NotifyNewOrdersMessage;
import ru.imagebook.client.admin.ctl.order.NotifyOrdersAcceptedMessage;
import ru.imagebook.client.admin.ctl.order.OrderMessages;
import ru.imagebook.client.admin.ctl.order.PublishWebFlashMessage;
import ru.imagebook.client.admin.ctl.order.RegenerateOrderMessage;
import ru.imagebook.client.admin.ctl.order.RegenerateOrderRequestMessage;
import ru.imagebook.client.admin.ctl.order.RegenerateOrderRequestResultMessage;
import ru.imagebook.client.admin.ctl.order.UpdateOrderMessage;
import ru.imagebook.client.admin.ctl.order.VendorUpdateOrderMessage;
import ru.imagebook.server.adm.flyleaf.FlyleafServiceI;
import ru.imagebook.server.adm.vellum.VellumServiceI;
import ru.imagebook.server.service.ActionService;
import ru.imagebook.server.service.OrderService;
import ru.imagebook.server.service.OrderService2;
import ru.imagebook.server.service.OrderStateIsNotAccepted;
import ru.imagebook.server.service.ProductService;
import ru.imagebook.server.service.TrialOrderExistsError;
import ru.imagebook.server.service.UserService;
import ru.imagebook.server.service.WrongOrderStateError;
import ru.imagebook.server.service.pdf.PdfService;
import ru.imagebook.shared.model.AdminSettings;
import ru.imagebook.shared.model.BonusAction;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.Flyleaf;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderFilter;
import ru.imagebook.shared.model.OrderState;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vellum;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.minogin.core.server.flow.remoting.MessageError;

public class AdminOrderController extends Controller {
	private final OrderService service;
	private final ProductService productService;
	private final ActionService actionService;
	private final OrderService2 service2;
	private final PdfService pdfService;
	private final UserService userService;
	private final FlyleafServiceI flyleafService;
	private final VellumServiceI vellumService;

	public AdminOrderController(Dispatcher dispatcher, final OrderService service,
			final ProductService productService, final ActionService actionService,
			OrderService2 service2, PdfService pdfService, UserService userService,
			FlyleafServiceI flyleafService, VellumServiceI vellumService) {
		super(dispatcher);

		this.service = service;
		this.productService = productService;
		this.actionService = actionService;
		this.service2 = service2;
		this.pdfService = pdfService;
		this.userService = userService;
		this.flyleafService = flyleafService;
		this.vellumService = vellumService;
	}

	@Override
	public void registerHandlers() {
		addHandler(OrderMessages.LOAD_ORDERS, (MessageHandler<LoadOrdersMessage>) message -> {
			int offset = message.getOffset();
			String query = message.getQuery();

			int userId = RemoteSessionMessage.getUserId(message);
			User user = userService.getUserLite(userId);
			AdminSettings adminSettings = (AdminSettings) user.getSettings();
			OrderFilter filter = adminSettings.getOrderFilter();

			List<Order<?>> orders = service.loadOrders(offset, message.getLimit(), filter, query);
			long total = service.countOrders(filter, query);
			send(new LoadOrdersResultMessage(orders, offset, total));
		});

		addHandler(OrderMessages.LOAD_DATA_FOR_ADD_FORM, message -> {
			Map<Integer, List<Product>> productsMap = service.loadProductsMap();
			List<Color> colors = productService.loadColors();
			List<BonusAction> actions = actionService.loadActions(null/*query*/);
			List<Flyleaf> flyleafs = flyleafService.list();
			List<Vellum> vellums = vellumService.list();
			send(new DataLoadedMessage(productsMap, colors, actions, flyleafs, vellums));
		});

		addHandler(OrderMessages.LOAD_USERS, (MessageHandler<LoadUsersMessage>) message -> {
			int offset = message.getOffset();
			List<User> users = service.loadUsers(offset, message.getLimit(), message.getQuery());
			long total = service.countUsers(message.getQuery());
			send(new LoadUsersResultMessage(users, offset, total));
		});

		addHandler(OrderMessages.ADD_ORDER, (MessageHandler<AddOrderMessage>) message -> {
			Order<?> order = message.getOrder();
			try {
				service.addOrder(order);
			}
			catch (TrialOrderExistsError e) {
				throw new MessageError(OrderMessages.TRIAL_ORDER_EXISTS);
			}
			catch (DataIntegrityViolationException e) {
				throw new MessageError(OrderMessages.ORDER_NUMBER_EXISTS);
			}

			Message reply = new BaseMessage(OrderMessages.ADD_ORDER_RESULT);
			reply.addAspects(RemotingAspect.CLIENT);
			send(reply);
		});

		addHandler(OrderMessages.UPDATE_ORDER, (MessageHandler<UpdateOrderMessage>) message -> {
			Order<?> order = message.getOrder();
			Order<?> currOrder = service.getOrder(order.getId());

			int oldState = currOrder.getState();
			int newState = order.getState();

			try {
				service.updateOrder(order);

				if (oldState != newState && newState == OrderState.REJECTED) {
					service.notifyUserOrderRejected(order.getId());
				}
			} catch (TrialOrderExistsError e) {
				throw new MessageError(OrderMessages.TRIAL_ORDER_EXISTS);
			}

			Message reply = new BaseMessage(OrderMessages.UPDATE_ORDER_RESULT);
			reply.addAspects(RemotingAspect.CLIENT);
			send(reply);
		});
		
		addHandler(OrderMessages.VENDOR_UPDATE_ORDER, new MessageHandler<VendorUpdateOrderMessage>() {
			private boolean isValid(Integer state) {
				return state == OrderState.PRINTING || state == OrderState.FINISHING ||
						state == OrderState.PRINTED || state == OrderState.DELIVERY ||
						state == OrderState.SENT;
			}
			
			@Override
			public void handle(VendorUpdateOrderMessage message) {
				Order<?> order = service.getOrder(message.getOrderId());
				int state = order.getState();
				if (isValid(state) && isValid(message.getState())) {
					order.setState(message.getState());
					
					Message reply = new BaseMessage(OrderMessages.UPDATE_ORDER_RESULT);
					reply.addAspects(RemotingAspect.CLIENT);
					send(reply);
				} else {
					throw new RuntimeException();
				}
			}
		});

		addHandler(OrderMessages.DELETE_ORDERS, (MessageHandler<DeleteOrdersMessage>) message -> {
			List<Integer> orderIds = message.getOrderIds();
			service.deleteOrders(orderIds);

			Message reply = new BaseMessage(OrderMessages.DELETE_ORDERS_RESULT);
			reply.addAspects(RemotingAspect.CLIENT);
			send(reply);
		});

		addHandler(OrderMessages.APPLY_FILTER, (MessageHandler<ApplyFilterMessage>) message -> {
			int userId = RemoteSessionMessage.getUserId(message);
			OrderFilter filter = message.getFilter();
			service.applyFilter(userId, filter);

			Message reply = new BaseMessage(OrderMessages.APPLY_FILTER_RESULT);
			reply.addAspects(RemotingAspect.CLIENT);
			send(reply);
		});

		addHandler(OrderMessages.NOTIFY_NEW_ORDERS, (MessageHandler<NotifyNewOrdersMessage>) message -> {
			try {
				service.notifyNewOrder(message.getOrderIds());
			}
			catch (WrongOrderStateError e) {
				throw new MessageError(OrderMessages.ORDER_STATE_IS_NOT_MODERATION);
			}

			Message reply = new BaseMessage(OrderMessages.NOTIFY_NEW_ORDERS_RESULT);
			reply.addAspects(RemotingAspect.CLIENT);
			send(reply);
		});

		addHandler(OrderMessages.NOTIFY_ORDERS_ACCEPTED,
				(MessageHandler<NotifyOrdersAcceptedMessage>) message -> {
					try {
						service.notifyFlashGenerated(message.getOrderIds());
					}
					catch (OrderStateIsNotAccepted e) {
						throw new MessageError(OrderMessages.ORDER_STATE_IS_NOT_ACCEPTED);
					}

					Message reply = new BaseMessage(OrderMessages.NOTIFY_ORDERS_ACCEPTED_RESULT);
					reply.addAspects(RemotingAspect.CLIENT);
					send(reply);
				});

		// addHandler(OrderMessages.EXPORT, new MessageHandler<Message>() {
		// @Override
		// public void handle(Message message) {
		// int userId = RemoteSessionMessage.getUserId(message);
		// TempFile file = service.exportOrders(userId);
		// send(RemoteDownloadMessage.createMessage(file));
		// }
		// });

		addHandler(OrderMessages.REGENERATE_ORDER_REQUEST, (MessageHandler<RegenerateOrderRequestMessage>) message -> {
			int orderId = message.getOrderId();
			service2.requestRegenerateOrder(orderId);

			send(new RegenerateOrderRequestResultMessage(orderId));
		});

		addHandler(OrderMessages.REGENERATE_ORDER, (MessageHandler<RegenerateOrderMessage>) message -> {
			int orderId = message.getOrderId();
			service2.regenerateOrder(orderId);
		});

		addHandler(OrderMessages.GENERATE_PDF, (MessageHandler<GeneratePdfMessage>) message -> {
			int orderId = message.getOrderId();
			pdfService.generateTestPdf(orderId);

			Message reply = new BaseMessage(OrderMessages.GENERATE_PDF_RESULT);
			reply.addAspects(RemotingAspect.CLIENT);
			send(reply);
		});

		addHandler(OrderMessages.PUBLISH_WEB_FLASH, (MessageHandler<PublishWebFlashMessage>) message -> {
			int orderId = message.getOrderId();
			service2.publishWebFlash(orderId);

			Message reply = new BaseMessage(OrderMessages.PUBLISH_WEB_FLASH_RESULT);
			reply.addAspects(RemotingAspect.CLIENT);
			send(reply);
		});
	}
}
