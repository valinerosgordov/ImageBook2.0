package ru.imagebook.server.ctl.editor;

import java.util.List;
import java.util.Map;

import ru.imagebook.client.editor.ctl.file.CancelShowNotificationMessage;
import ru.imagebook.client.editor.ctl.file.RequireShowNotificationMessage;
import ru.imagebook.client.editor.ctl.file.ShowNotificationMessage;
import ru.imagebook.client.editor.ctl.file.*;
import ru.imagebook.client.editor.ctl.order.*;
import ru.imagebook.client.editor.ctl.pages.*;
import ru.imagebook.server.ctl.RemoteSessionMessage;
import ru.imagebook.server.service.editor.EditorService;
import ru.imagebook.server.service.editor.EditorServiceCommonError;
import ru.imagebook.server.service.editor.UploadMessage;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.editor.ImageLayoutType;
import ru.imagebook.shared.model.editor.Page;
import ru.minogin.core.client.file.FileBean;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class EditorController extends Controller {
	private final EditorService service;

	public EditorController(Dispatcher dispatcher, EditorService service) {
		super(dispatcher);

		this.service = service;
	}

	@Override
	public void registerHandlers() {
		addHandler(FileMessages.LOAD_FOLDERS, new MessageHandler<LoadFoldersMessage>() {
			@Override
			public void handle(LoadFoldersMessage message) {
				int userId = RemoteSessionMessage.getUserId(message);
				FileBean root = service.loadFolders(userId);
				send(new LoadFoldersResultMessage(root));
			}
		});

		addHandler(FileMessages.LOAD_IMAGES, new MessageHandler<LoadImagesMessage>() {
			@Override
			public void handle(LoadImagesMessage message) {
				int userId = RemoteSessionMessage.getUserId(message);
				List<String> paths = service.loadPreviews(userId, message.getPath());
				send(new LoadImagesResultMessage(paths));
			}
		});

		addHandler(RemoteEditorMessages.SHOW_PREVIEW, new MessageHandler<ShowPreviewMessage>() {
			@Override
			public void handle(ShowPreviewMessage message) {
				int userId = RemoteSessionMessage.getUserId(message);
				service.showPreview(userId, message.getPath(), message.getOutputStream());
			}
		});

		addHandler(RemoteEditorMessages.SHOW_COMPONENT, new MessageHandler<ShowComponentMessage>() {
			@Override
			public void handle(ShowComponentMessage message) {
				String sessionId = SessionAspect.getSessionId(message);
				int userId = RemoteSessionMessage.getUserId(message);
				service.showComponent(sessionId, userId, message.getComponentId(), message
						.getOutputStream());
			}
		});

		addHandler(RemoteEditorMessages.UPLOAD, new MessageHandler<UploadMessage>() {
			@Override
			public void handle(UploadMessage message) {
				String sessionId = SessionAspect.getSessionId(message);
				int userId = RemoteSessionMessage.getUserId(message);
				service.upload(message.getRequest(), sessionId, userId);
			}
		});

		addHandler(OrderMessages.LOAD_PRODUCTS, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				int userId = RemoteSessionMessage.getUserId(message);
				Map<Integer, List<Product>> products = service.loadProductsMap(userId);
				List<Order<?>> orders = service.loadAllOrders(userId);
				send(new LoadProductsResultMessage(products, orders));
			}
		});

		addHandler(OrderMessages.CREATE_ORDER, new MessageHandler<CreateOrderMessage>() {
			@Override
			public void handle(CreateOrderMessage message) {
				String sessionId = SessionAspect.getSessionId(message);
				int userId = RemoteSessionMessage.getUserId(message);
				Order<?> order = service.createOrder(sessionId, userId, message.getProductId(), message
						.getPageCount());
				send(new CreateOrderResultMessage(order));
			}
		});

		addHandler(OrderMessages.COPY_ORDER, new MessageHandler<CopyOrderMessage>() {
			@Override
			public void handle(CopyOrderMessage message) {
				String sessionId = SessionAspect.getSessionId(message);
				int userId = RemoteSessionMessage.getUserId(message);
				Order<?> order = service.copyOrder(sessionId, userId, message.getOrderId());
				send(new CreateOrderResultMessage(order));
			}
		});

		addHandler(FileMessages.ADD_IMAGE, new MessageHandler<AddImageMessage>() {
			@Override
			public void handle(AddImageMessage message) {
				String sessionId = SessionAspect.getSessionId(message);
				int userId = RemoteSessionMessage.getUserId(message);
				Map<Integer, Page> changedPages;
				if (message.getType() < ImageLayoutType.FOLDER_NORMAL) {
					changedPages = service.addImage(sessionId, userId, message.getPath(),
							message.getType(), message.getPageNumber());
				} else {
					try {
						Order<?> order = service.addFolderWithImages(sessionId, userId, message.getPath(),
								message.getType(), message.getPageNumber());
						//order = service.openOrder(sessionId, userId, order.getId());
						send(new OpenOrderResultMessage(order));
					} catch (EditorServiceCommonError e) {
						send( new ShowEditorCommonErrorMessage(e.getErrorMessage()));
					}
					return;
				}
				for (int number : changedPages.keySet()) {
					send(new PageChangedMessage(number, changedPages.get(number)));
				}
			}
		});

		addHandler(FileMessages.SHOW_NOTIFICATION, new MessageHandler<ShowNotificationMessage>() {
			@Override
			public void handle(ShowNotificationMessage message) {
				final int userId = RemoteSessionMessage.getUserId(message);
				final String sessionId = SessionAspect.getSessionId(message);
				final boolean isRequired = service.requiredShowNotification(sessionId, userId, message.getType(), message.isCheckOnlyForCommonOrder());
				send (new RequireShowNotificationMessage(message.getType(), message.getImageLayoutType(), isRequired));
			}
		});

		addHandler(PagesMessages.CHANGE_PAGE_TYPE_TO_INDIVIDUAL_NOTIFICATION, new MessageHandler<ConvertToIndividualPageNotificationMessage>() {
			@Override
			public void handle(ConvertToIndividualPageNotificationMessage message) {
				final int userId = RemoteSessionMessage.getUserId(message);
				final String sessionId = SessionAspect.getSessionId(message);
				final boolean isRequired = service.requiredShowNotification(sessionId, userId, message.getType(), false);
				send (new RequireShowConvertPageTypeNotificationMessage(message.getType(), message.getPage(), message.getPath(), message.getPageNumber(), isRequired));
			}
		});

		addHandler(FileMessages.CANCEL_SHOW_NOTIFICATION, new MessageHandler<CancelShowNotificationMessage>() {
			@Override
			public void handle(CancelShowNotificationMessage message) {
				final int userId = RemoteSessionMessage.getUserId(message);
				service.cancelShowNotificationMessage(userId, message.getType());
			}
		});

		addHandler(OrderMessages.LOAD_ORDERS, new MessageHandler<LoadOrdersMessage>() {
			@Override
			public void handle(LoadOrdersMessage message) {
				int userId = RemoteSessionMessage.getUserId(message);
				List<Order<?>> orders = service.loadOrders(userId);
				send(new LoadOrdersResultMessage(orders));
			}
		});

		addHandler(OrderMessages.OPEN_ORDER, new MessageHandler<OpenOrderMessage>() {
			@Override
			public void handle(OpenOrderMessage message) {
				String sessionId = SessionAspect.getSessionId(message);
                int userId = RemoteSessionMessage.getUserId(message);
				Order<?> order = service.openOrder(sessionId, userId, message.getOrderId());
				send(new OpenOrderResultMessage(order));
			}
		});

		addHandler(FileMessages.CREATE_FOLDER, new MessageHandler<CreateFolderMessage>() {
			@Override
			public void handle(CreateFolderMessage message) {
				int userId = RemoteSessionMessage.getUserId(message);
				service.createFolder(userId, message.getPath(), message.getName());

				BaseMessage reply = new BaseMessage(FileMessages.CREATE_FOLDER_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(FileMessages.EDIT_FOLDER, new MessageHandler<EditFolderMessage>() {
			@Override
			public void handle(EditFolderMessage message) {
				int userId = RemoteSessionMessage.getUserId(message);
				service.editFolder(userId, message.getPath(), message.getName());

				BaseMessage reply = new BaseMessage(FileMessages.EDIT_FOLDER_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(FileMessages.DELETE_FOLDER, new MessageHandler<DeleteFolderMessage>() {
			@Override
			public void handle(DeleteFolderMessage message) {
				int userId = RemoteSessionMessage.getUserId(message);
				service.deleteFolder(userId, message.getPath());

				BaseMessage reply = new BaseMessage(FileMessages.DELETE_FOLDER_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(OrderMessages.PROCESS_ORDER, new MessageHandler<ProcessOrderMessage>() {
			@Override
			public void handle(ProcessOrderMessage message) {
				String sessionId = SessionAspect.getSessionId(message);
				int userId = RemoteSessionMessage.getUserId(message);
				service.processOrder(sessionId, userId);

				BaseMessage reply = new BaseMessage(OrderMessages.PROCESS_ORDER_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(FileMessages.DELETE_IMAGE, new MessageHandler<DeleteImageMessage>() {
			@Override
			public void handle(DeleteImageMessage message) {
				int userId = RemoteSessionMessage.getUserId(message);
				service.deleteImage(userId, message.getPath());

				BaseMessage reply = new BaseMessage(FileMessages.DELETE_IMAGE_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(OrderMessages.CLOSE_ORDER, new MessageHandler<CloseOrderMessage>() {
			@Override
			public void handle(CloseOrderMessage message) {
				String sessionId = SessionAspect.getSessionId(message);
				service.removeOrderFromSession(sessionId);
			}
		});

		addHandler(PagesMessages.CHANGE_PAGE_COUNT, new MessageHandler<ChangePageCountMessage>() {
			@Override
			public void handle(ChangePageCountMessage message) {
				String sessionId = SessionAspect.getSessionId(message);
				int userId = RemoteSessionMessage.getUserId(message);
				Order<?> order = service.changePageCount(sessionId, userId, message.getPageCount());
				send(new ChangePageCountResultMessage(order));
			}
		});

		addHandler(PagesMessages.CHANGE_CURRENT_ORDER_LAYOUT, new MessageHandler<ChangeCurrentOrderLayoutMessage>() {
			@Override
			public void handle(ChangeCurrentOrderLayoutMessage message) {
				String sessionId = SessionAspect.getSessionId(message);
				int userId = RemoteSessionMessage.getUserId(message);
				service.changeCurrentOrderLayout(sessionId, userId, message.getLayout());
				Order<?> order = service.loadingOrderWithLayoutsAndPages(sessionId, userId);
				send(new ChangeCurrentOrderLayoutResultMessage(order));
			}
		});

		addHandler(PagesMessages.CHANGE_PAGE_TYPE_TO_INDIVIDUAL_REQUEST, new MessageHandler<ChangePageTypeOnIndividualMessage>() {
			@Override
			public void handle(ChangePageTypeOnIndividualMessage message) {
				String sessionId = SessionAspect.getSessionId(message);
				int userId = RemoteSessionMessage.getUserId(message);
				Order<?> order = service.changePageTypeToIndividual(sessionId, userId, message.getPage(), message.getPath(), message.getPageNumber());
				send(new ChangeCurrentOrderLayoutResultMessage(order));
			}
		});

		addHandler(PagesMessages.CLEAR_SPREAD, new MessageHandler<ClearSpreadMessage>() {
			@Override
			public void handle(ClearSpreadMessage message) {
				String sessionId = SessionAspect.getSessionId(message);
				int userId = RemoteSessionMessage.getUserId(message);
				int number = message.getPageNumber();
				Page page = service.clearSpread(sessionId, userId, number);
				send(new PageChangedMessage(number, page));
			}
		});

		addHandler(FileMessages.DISPOSE, new MessageHandler<DisposeMessage>() {
			@Override
			public void handle(DisposeMessage message) {
				String sessionId = SessionAspect.getSessionId(message);
				int userId = RemoteSessionMessage.getUserId(message);
				Order<?> order = service.dispose(sessionId, userId, message.getPath());
				send(new DisposeResultMessage(order));
			}
		});
	}
}
