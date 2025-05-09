package ru.imagebook.client.admin.ctl.action;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.admin.view.action.ActionPresenter;
import ru.imagebook.client.common.service.I18nService;
import ru.imagebook.client.common.service.admin.Actions;
import ru.imagebook.client.common.service.delivery.DeliveryDiscountServiceAsync;
import ru.imagebook.client.common.service.security.SecurityService;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.BonusAction;
import ru.imagebook.shared.model.DeliveryDiscount;
import ru.imagebook.shared.service.admin.delivery.DeliveryDiscountExistsException;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.minogin.core.client.gwt.DateFormat;
import ru.minogin.util.client.rpc.XAsyncCallback;
import ru.saasengine.client.ctl.auth.SessionAspect;
import ru.saasengine.client.service.auth.AuthService;

@Singleton
public class ActionController extends Controller implements ActionPresenter{
	private final ActionView view;
	private final AuthService authService;
	private final I18nService i18nService;
	private final SecurityService securityService;
	private final DeliveryDiscountServiceAsync deliveryDiscountService;

	private Integer currentVendorId;
	private String query;
	
	@Inject
	public ActionController(Dispatcher dispatcher, final ActionView view, AuthService authService,
							I18nService i18nService, SecurityService securityService,
							DeliveryDiscountServiceAsync deliveryDiscountService) {
		super(dispatcher);
		this.view = view;
		view.setPresenter(this);
		this.authService = authService;
		this.i18nService = i18nService;
		this.securityService = securityService;
		this.deliveryDiscountService = deliveryDiscountService;
	}

	@Override
	public void registerHandlers() {
		addHandler(ActionMessages.SHOW_ACTIONS, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				query = null;
				view.showSection();

				if (securityService.isAllowed(Actions.REQUEST_STATUS)) {
					send(ActionMessages.SHOW_STATUS_SECTION);
				} else {
					send(ActionMessages.SHOW_ACTIONS_SECTION);
				}
			}
		});
		
		addHandler(ActionMessages.SHOW_ACTIONS_SECTION, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				if (securityService.isAllowed(Actions.VIEW_ALL_VENDORS)) {
					Message request = new BaseMessage(ActionMessages.SHOW_ACTIONS_SECTION_WITH_VENDORS);
					request.addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
					send(request);
				} else {
					send(new ShowActionsSectionResultMessage(null));
				}
			}
		});

		addHandler(ActionMessages.SHOW_ACTIONS_SECTION_RESULT, new MessageHandler<ShowActionsSectionResultMessage>() {
			@Override
			public void handle(ShowActionsSectionResultMessage message) {
				view.showActionSection(message.getVendors());

				if (message.getVendors() == null) {
					send(new LoadActionsMessage(currentVendorId, query));
				}
			}
		});

		addHandler(ActionMessages.LOAD_VENDOR_ACTIONS, new MessageHandler<LoadVendorActionsMessage>() {
			@Override
			public void handle(LoadVendorActionsMessage message) {
				currentVendorId = message.getVendorId();
				send(new LoadActionsMessage(currentVendorId, query));
			}
		});

		addHandler(ActionMessages.LOAD_ALBUMS_RESULT, new MessageHandler<LoadAlbumsResultForBonusActionMessage>() {
			@Override
			public void handle(LoadAlbumsResultForBonusActionMessage message) {
				final List<Album> albumList = message.getAlbums();
				view.showActionForm(albumList, message.getBonusAction());
			}
		});

		addHandler(ActionMessages.LOAD_ACTIONS_RESULT, new MessageHandler<LoadActionsResultMessage>() {
			@Override
			public void handle(LoadActionsResultMessage message) {
				for (BonusAction item : message.getActions()) {
					item.setTransient(BonusAction.DATE_START_FIELD, DateFormat.formatDate(item.getDateStart()));
					item.setTransient(BonusAction.DATE_END_FIELD, DateFormat.formatDate(item.getDateEnd()));
				}
				view.showActions(message.getActions());
			}
		});

		addHandler(ActionMessages.LOAD_BONUS_CODES_WITH_ORDER_INFO_RESULT, new MessageHandler<LoadBonusCodesWithOrderInfoResultMessage>() {
			@Override
			public void handle(LoadBonusCodesWithOrderInfoResultMessage message) {
				view.showBonusCodes(message.getBonusCodeOrders());
			}
		});

		addHandler(ActionMessages.ADD_ACTION_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.hideAddForm();
				send(new LoadActionsMessage(currentVendorId, query));
			}
		});

		addHandler(ActionMessages.UPDATE_ACTION_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.hideEditForm();
				send(new LoadActionsMessage(currentVendorId, query));
			}
		});

		addHandler(ActionMessages.DELETE_ACTION_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadActionsMessage(currentVendorId, query));
			}
		});

		addHandler(ActionMessages.DELETE_BONUS_CODES_ACTIONS_RESULT, new MessageHandler<DeleteBonusCodesResultMessage>() {
			@Override
			public void handle(DeleteBonusCodesResultMessage message) {
				view.loadBonusCodes();
			}
		});

		addHandler(ActionMessages.BONUS_CODE_ORDER_EXISTS, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.alertBonusCodeOrderExists();
			}
		});

		addHandler(ActionMessages.SHOW_CODES, new MessageHandler<ShowCodesMessage>() {
			@Override
			public void handle(ShowCodesMessage message) {
				BonusAction action = message.getAction();
				view.showCodes(action, authService.getSessionId());
			}
		});

		addHandler(ActionMessages.SHOW_BONUS_CODES, new MessageHandler<ShowBonusCodesByActionMessage>() {
			@Override
			public void handle(ShowBonusCodesByActionMessage message) {
				BonusAction action = message.getAction();
				send(new LoadBonusCodesWithOrderInfoMessage(message.getAction().getId()));
			}
		});

		addHandler(ActionMessages.GENERATE_CODES_REQUEST,
				new MessageHandler<GenerateCodesRequestMessage>() {
					@Override
					public void handle(GenerateCodesRequestMessage message) {
						view.showGenerateForm(message.getAction());
					}
				});

		addHandler(ActionMessages.GENERATE_CODES_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.informGenerateResult();
			}
		});

		addHandler(ActionMessages.SHOW_STATUS_SECTION, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.showStatusSection();

				send(new LoadStatusRequestsMessage());
			}
		});

		addHandler(ActionMessages.LOAD_STATUS_REQUESTS_RESULT,
				new MessageHandler<LoadStatusRequestsResultMessage>() {
					@Override
					public void handle(LoadStatusRequestsResultMessage message) {
						view.showStatusRequests(message.getCodes(), i18nService.getLocale());
					}
				});

		addHandler(ActionMessages.OPEN_STATUS_REQUEST, new MessageHandler<OpenStatusRequestMessage>() {
			@Override
			public void handle(OpenStatusRequestMessage message) {
				view.openRequest(message.getRequest());
			}
		});

		addHandler(ActionMessages.SHOW_REJECT_FORM, new MessageHandler<ShowRejectFormMessage>() {
			@Override
			public void handle(ShowRejectFormMessage message) {
				view.showRejectForm(message.getRequest());
			}
		});

		addHandler(ActionMessages.SHOW_SEND_FORM, new MessageHandler<ShowSendFormMessage>() {
			@Override
			public void handle(ShowSendFormMessage message) {
				view.showSendForm(message.getRequest());
			}
		});

		addHandler(ActionMessages.SEND_STATUS_CODE_RESULT,
				new MessageHandler<SendStatusCodeResultMessage>() {
					@Override
					public void handle(SendStatusCodeResultMessage message) {
						view.hideSendDialogs();

						send(new LoadStatusRequestsMessage());
					}
				});

		addHandler(ActionMessages.REJECT_REQUEST_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadStatusRequestsMessage());
			}
		});

		addHandler(ActionMessages.ADD_CODE_RESULT, new MessageHandler<AddCodeResultMessage>() {
			@Override
			public void handle(AddCodeResultMessage message) {
				if (message.getResult()) {
					view.hideAddCodesForm();
				} else {
					view.informBadCode();
				}
			}
		});

		addHandler(ActionMessages.SEARCH, new MessageHandler<SearchActionCodeMessage>() {
			@Override
			public void handle(SearchActionCodeMessage message) {
				query = message.getQuery();
				send(new LoadActionsMessage(currentVendorId, query));
			}
		});
	}

    @Override
    public void showDeliverySection() {
        deliveryDiscountService.loadDeliveryDiscounts(new XAsyncCallback<List<DeliveryDiscount>>() {
            @Override
            public void onSuccess(List<DeliveryDiscount> deliveryDiscounts) {
                view.showDeliverySection(deliveryDiscounts);
            }
        });
    }

	@Override
	public void addDeliveryDiscount(final DeliveryDiscount deliveryDiscount) {
		deliveryDiscountService.addDeliveryDiscount(deliveryDiscount, new XAsyncCallback<Integer>() {
            @Override
            public void onSuccess(Integer id) {
                deliveryDiscount.setId(id);
                view.addDeliveryDiscountToGrid(deliveryDiscount);
            }

			@Override
			public void onFailure(Throwable throwable) {
                showDeliverySection();
				if (throwable instanceof DeliveryDiscountExistsException) {
					view.alertAddingDeliveryDiscountError();
				} else {
					super.onFailure(throwable);
				}
			}
		});
	}

	@Override
	public void updateDeliveryDiscount(DeliveryDiscount deliveryDiscount) {
		deliveryDiscountService.updateDeliveryDiscount(deliveryDiscount, new XAsyncCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }

			@Override
			public void onFailure(Throwable throwable) {
				showDeliverySection();
				if (throwable instanceof DeliveryDiscountExistsException) {
					view.alertDeliveryDiscountExists();
				} else {
					super.onFailure(throwable);
				}
			}
		});
	}

	@Override
	public void deleteDeliveryDiscount(Integer id) {
		deliveryDiscountService.deleteDeliveryDiscount(id, new XAsyncCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }

			@Override
			public void onFailure(Throwable throwable) {
				showDeliverySection();
			}
		});
	}
}
