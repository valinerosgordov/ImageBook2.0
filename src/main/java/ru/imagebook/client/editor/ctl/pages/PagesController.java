package ru.imagebook.client.editor.ctl.pages;

import java.util.List;

import ru.imagebook.client.editor.ctl.file.ConvertToIndividualPageNotificationMessage;
import ru.imagebook.client.editor.ctl.file.FileMessages;
import ru.imagebook.client.editor.ctl.file.RequireShowConvertPageTypeNotificationMessage;
import ru.imagebook.client.editor.ctl.order.OpenOrderResultMessage;
import ru.imagebook.client.editor.ctl.order.OrderMessages;
import ru.imagebook.client.editor.ctl.order.PageChangedMessage;
import ru.imagebook.client.editor.ctl.order.ShowOrderMessage;
import ru.imagebook.client.editor.ctl.spread.ShowSpreadPageMessage;
import ru.imagebook.client.editor.service.EditorService;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.editor.Layout;
import ru.imagebook.shared.model.editor.NotificationType;
import ru.imagebook.shared.model.editor.Page;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.saasengine.client.service.auth.AuthService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class PagesController extends Controller {
	private final PagesView view;
	private final AuthService authService;
	private final EditorService service;

	@Inject
	public PagesController(Dispatcher dispatcher, PagesView view, AuthService authService,
			EditorService service) {
		super(dispatcher);

		this.view = view;
		this.authService = authService;
		this.service = service;
	}

	@Override
	public void registerHandlers() {
		addHandler(OrderMessages.SHOW_ORDER, new MessageHandler<ShowOrderMessage>() {
			@Override
			public void handle(ShowOrderMessage message) {
				Order<?> order = message.getOrder();

				view.showOrder(order);

				Layout layout = order.getLayout();
				List<Page> pages = layout.getPages();
				for (int i = 0; i < pages.size(); i++) {
					send(new ShowPageMessage(order, i));
				}

				send(new SelectPageMessage(0, service.getVariant()));

				view.showMenu(service.getOrder(), service.getVariant());
			}
		});

		addHandler(PagesMessages.SHOW_PAGE, new MessageHandler<ShowPageMessage>() {
			@Override
			public void handle(ShowPageMessage message) {
				view.showPage((AlbumOrder) message.getOrder(), message.getPageNumber(), authService
						.getSessionId());
			}
		});

		addHandler(PagesMessages.SELECT_PAGE, new MessageHandler<SelectPageMessage>() {
			@Override
			public void handle(SelectPageMessage message) {
				int currentPage = message.getPageNumber();
				service.setPageNumber(currentPage);
				service.setVariant(message.getVariant());

				view.selectPage(message.getPageNumber());

				AlbumOrder order = service.getOrder();
				send(new ShowSpreadPageMessage(order, currentPage));
				/*ищем текущую страницу по ее номеру и слою, и если она общего типа, то показываем кнопку "сделать индивидуальным"*/
				int index = 0;
				if (order.isPackaged()) {
					for (final Layout layout : order.getLayouts()) {
						if (index == service.getVariant()) {
							final Page page = layout.getPages().get(currentPage);
							view.activateConvertToIndividualPageButton(page != null && page.isCommon());
							break;
						}
						index++;
					}
				}
				send(FileMessages.SHOW_IMAGE_MENU);
			}
		});

		addHandler(FileMessages.REQUIRE_CONVERTING_NOTIFICATION, new MessageHandler<RequireShowConvertPageTypeNotificationMessage>() {
			@Override
			public void handle(RequireShowConvertPageTypeNotificationMessage message) {
				view.showConvertingMessage(message.getType(), message.getPage(), message.getPath(), message.getPageNumber(), message.isShowMessage());
			}
		});

		addHandler(OrderMessages.CLOSE_ORDER, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.hidePages();

				view.hideMenu();
			}
		});

		addHandler(PagesMessages.CHANGE_PAGE_COUNT_REQUEST, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.showPageCountWindow(service.getOrder());
			}
		});

		addHandler(PagesMessages.CHANGE_PAGE_TYPE_TO_INDIVIDUAL, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				final int pageNumber = service.getPageNumber();
				final int layoutVariant = service.getVariant();
				int index = 0;
				for (final Layout layout : service.getOrder().getLayouts()) {
					if (index == layoutVariant) {
						final Page page = layout.getPages().get(pageNumber);
						if (page != null && page.isCommon()) {
							send(new ConvertToIndividualPageNotificationMessage(page, service.getImagePath(), service.getPageNumber(), NotificationType.CHANGE_COMMON_ON_INDIVIDUAL_PAGE.getType()));
						}
						break;
					}
					index++;
				}
			}
		});

		addHandler(PagesMessages.CHANGE_PAGE_COUNT_RESULT,
				new MessageHandler<ChangePageCountResultMessage>() {
					@Override
					public void handle(ChangePageCountResultMessage message) {
						AlbumOrder order = (AlbumOrder) message.getOrder();
						service.setOrder(order);

						view.showPageCountWarning();
						view.hidePageCountWindow();

						send(new ShowOrderMessage(order));

						send(OrderMessages.ORDER_SHOWN);
					}
				});

		addHandler(PagesMessages.CHANGE_CURRENT_ORDER_LAYOUT_RESULT,
				new MessageHandler<ChangeCurrentOrderLayoutResultMessage>() {
					@Override
					public void handle(ChangeCurrentOrderLayoutResultMessage message) {
						AlbumOrder order = (AlbumOrder) message.getOrder();
						service.setOrder(order);
						view.showOrder(order);

						Layout layout = order.getLayouts().get(service.getVariant());
						List<Page> pages = layout.getPages();
						for (int i = 0; i < pages.size(); i++) {
							send(new PageChangedMessage(i, pages.get(i)));
						}

						/*send(new SelectPageMessage(0, service.getVariant()));

						view.showMenu(service.getOrder(), service.getVariant());*/
					}
				});

		addHandler(PagesMessages.CLEAR_SPREAD, new MessageHandler<ClearSpreadMessage>() {
			@Override
			public void handle(ClearSpreadMessage message) {
				Layout layout = service.getOrder().getLayout();
				int pageNumber = service.getPageNumber();
				Page page = layout.getPages().get(pageNumber);
				if (page.isBlocked()) {
					message.cancel();
					return;
				}
				message.setPageNumber(pageNumber);
			}
		});

		addHandler(PagesMessages.SELECT_LAYOUT, new MessageHandler<SelectLayoutMessage>() {
			@Override
			public void handle(SelectLayoutMessage message) {
				service.setVariant(message.getVariant());
				try {
					if (service.getPageNumber() < 0) service.setPageNumber(0);
				} catch (Exception e) {
					service.setPageNumber(0);
				}
				AlbumOrder order = service.getOrder();
				order.setLayout(message.getlayout());

				send(new ChangeCurrentOrderLayoutMessage(order.getLayout()));

				//view.showOrder(order);

				//send(new ShowSpreadPageMessage(order, service.getPageNumber()));
			}
		});
	}
}
