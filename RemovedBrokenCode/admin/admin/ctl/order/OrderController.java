package ru.imagebook.client.admin.ctl.order;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.admin.service.ClientOrderService;
import ru.imagebook.client.admin.service.ClientOrderServiceAsync;
import ru.imagebook.client.admin.view.order.OrderPresenter;
import ru.imagebook.client.common.service.I18nService;
import ru.imagebook.client.common.service.UserService;
import ru.imagebook.client.common.service.order.OrderService;
import ru.imagebook.client.common.service.security.SecurityService;
import ru.imagebook.client.common.service.vendor.VendorService;
import ru.imagebook.shared.model.*;
import ru.minogin.core.client.app.failure.XAsyncCallback;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;
import ru.saasengine.client.service.auth.AuthService;

@Singleton
public class OrderController extends Controller implements OrderPresenter {
    private final OrderView view;
    private final I18nService i18nService;
    private final UserService userService;
    private final OrderService orderService;
    private final VendorService vendorService;
    private final SecurityService securityService;
    private final AuthService authService;
    private final ClientOrderServiceAsync service = GWT.create(ClientOrderService.class);

    private List<Color> colors;
    private List<BonusAction> actions;
    private List<Flyleaf> flyleafs;
    private List<Vellum> vellums;
    private boolean isAddForm;
    private boolean isCopyForm;
    private Order<?> order;
    private String query;

    @Inject
    public OrderController(Dispatcher dispatcher, OrderView view, I18nService i18nService, UserService userService,
                           SecurityService securityService, OrderService orderService, VendorService vendorService,
                           AuthService authService) {
        super(dispatcher);
        this.view = view;
        view.setPresenter(this);
        this.i18nService = i18nService;
        this.userService = userService;
        this.securityService = securityService;
        this.orderService = orderService;
        this.vendorService = vendorService;
        this.authService = authService;
    }

    @Override
    public void registerHandlers() {
        addHandler(OrderMessages.SHOW_ORDERS, new MessageHandler<Message>() {
            @Override
            public void handle(Message message) {
                query = null;
                boolean canDelete = securityService.hasRole(Role.ADMIN);
                boolean colorize = !securityService.hasRole(Role.VENDOR) || securityService.isRoot();
                view.showOrdersSection(colorize, canDelete, authService.getSessionId());
            }
        });

        addHandler(OrderMessages.LOAD_ORDERS, new MessageHandler<LoadOrdersMessage>() {
            @Override
            public void handle(LoadOrdersMessage message) {
                message.setQuery(query);
            }
        });

        addHandler(OrderMessages.LOAD_ORDERS_RESULT, new MessageHandler<LoadOrdersResultMessage>() {
            @Override
            public void handle(LoadOrdersResultMessage message) {
                User user = userService.getUser();
                AdminSettings adminSettings = (AdminSettings) user.getSettings();
                adminSettings = (AdminSettings) user.getSettings();
                view.showOrders(message.getOrders(), message.getOffset(), (int) message.getTotal(), i18nService.getLocale(),
                        adminSettings.getOrderFilter());
            }
        });

        addHandler(OrderMessages.SHOW_ADD_FORM, new MessageHandler<Message>() {
            @Override
            public void handle(Message message) {
                isAddForm = true;
                Message msg = new BaseMessage(OrderMessages.LOAD_DATA_FOR_ADD_FORM);
                msg.addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
                send(msg);
            }
        });

        addHandler(OrderMessages.DATA_LOADED, new MessageHandler<DataLoadedMessage>() {
            @Override
            public void handle(DataLoadedMessage message) {
                colors = message.getColors();
                actions = message.getActions();
                flyleafs = message.getFlyleafs();
                vellums = message.getVellums();
                if (isAddForm) {
                    isAddForm = false;
                    view.showProductSelectForm(message.getProducts(), i18nService.getLocale());
                } else if (isCopyForm) {
                    isCopyForm = false;
                    view.showAddForm(order, i18nService.getLocale(), colors, actions, flyleafs, vellums);
                } else
                    view.showEditForm(order, i18nService.getLocale(), colors, actions, flyleafs, vellums);
            }
        });

        addHandler(OrderMessages.PRODUCT_SELECTED, new MessageHandler<ProductSelectedMessage>() {
            @Override
            public void handle(ProductSelectedMessage message) {
                Album album = (Album) message.getProduct();
                AlbumOrder order = new AlbumOrderImpl(album);
                order.setState(OrderState.FLASH_GENERATED);

                view.showAddForm(order, i18nService.getLocale(), colors, actions, flyleafs, vellums);
            }
        });

        addHandler(OrderMessages.LOAD_USERS_RESULT, new MessageHandler<LoadUsersResultMessage>() {
            @Override
            public void handle(LoadUsersResultMessage message) {
                view.showUsers(message.getUsers(), message.getOffset(), (int) message.getTotal());
            }
        });

        addHandler(OrderMessages.ADD_ORDER_RESULT, new MessageHandler<Message>() {
            @Override
            public void handle(Message message) {
                view.reload();
                view.hideAddForm();
            }
        });

        addHandler(OrderMessages.DELETE_ORDERS_REQUEST, new MessageHandler<DeleteOrdersRequestMessage>() {
            @Override
            public void handle(DeleteOrdersRequestMessage message) {
                List<Order<?>> orders = message.getOrders();
                if (!orders.isEmpty())
                    view.confirmDeleteOrders(orders);
                else
                    view.alertNoOrdersToDelete();
            }
        });

        addHandler(OrderMessages.DELETE_ORDERS, new MessageHandler<DeleteOrdersMessage>() {
            @Override
            public void handle(DeleteOrdersMessage message) {
                List<Integer> orderIds = new ArrayList<Integer>();
                for (Order<?> order : message.getOrders()) {
                    orderIds.add(order.getId());
                }
                message.setOrderIds(orderIds);
            }
        });

        addHandler(OrderMessages.DELETE_ORDERS_RESULT, new MessageHandler<Message>() {
            @Override
            public void handle(Message message) {
                view.reload();
            }
        });

        addHandler(OrderMessages.SHOW_EDIT_FORM, new MessageHandler<ShowEditFormMessage>() {
            @Override
            public void handle(ShowEditFormMessage message) {
                order = message.getOrder();
                isCopyForm = false;
                Message msg = new BaseMessage(OrderMessages.LOAD_DATA_FOR_ADD_FORM);
                msg.addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
                send(msg);
            }
        });

        addHandler(OrderMessages.UPDATE_ORDER_RESULT, new MessageHandler<Message>() {
            @Override
            public void handle(Message message) {
                view.reload();
                view.hideEditForm();
            }
        });

        addHandler(OrderMessages.SHOW_FILTER, new MessageHandler<Message>() {
            @Override
            public void handle(Message message) {
                User user = userService.getUser();
                final AdminSettings adminSettings = (AdminSettings) user.getSettings();
                service.loadVendors(new XAsyncCallback<List<Vendor>>() {
                    @Override
                    public void onSuccess(List<Vendor> vendors) {
                        view.showFilter(adminSettings.getOrderFilter(), i18nService.getLocale(), vendors);
                    }
                });
            }
        });

        addHandler(OrderMessages.APPLY_FILTER_RESULT, new MessageHandler<Message>() {
            @Override
            public void handle(Message message) {
                view.reload();
            }
        });

        addHandler(OrderMessages.COPY_ORDER, new MessageHandler<CopyOrderMessage>() {
            @Override
            public void handle(CopyOrderMessage message) {
                Order<?> prototype = message.getOrder();
                isCopyForm = true;
                order = new AlbumOrderImpl((Album) prototype.getProduct());
                order.setUser(prototype.getUser());
                order.setState(OrderState.FLASH_GENERATED);
                order.setColor(prototype.getColor());
                order.setPageCount(prototype.getPageCount());
                order.setCoverLamination(prototype.getCoverLamination());
                order.setPageLamination(prototype.getPageLamination());
                order.setTrial(prototype.isTrial());
                order.setItemWeight(prototype.getItemWeight());
                order.setTotalWeight(prototype.getTotalWeight());
                order.setUrgent(prototype.isUrgent());
                Message msg = new BaseMessage(OrderMessages.LOAD_DATA_FOR_ADD_FORM);
                msg.addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
                send(msg);
            }
        });

        addHandler(OrderMessages.NOTIFY_NEW_ORDERS_RESULT, new MessageHandler<Message>() {
            @Override
            public void handle(Message message) {
                view.infoNotifyNewOrderResult();
            }
        });

        addHandler(OrderMessages.ORDER_STATE_IS_NOT_MODERATION, new MessageHandler<Message>() {
            @Override
            public void handle(Message message) {
                view.alertOrderStateIsNotModeration();
            }
        });

        addHandler(OrderMessages.NOTIFY_ORDERS_ACCEPTED_RESULT, new MessageHandler<Message>() {
            @Override
            public void handle(Message message) {
                view.infoNotifyOrdersAcceptedResult();
            }
        });

        addHandler(OrderMessages.ORDER_STATE_IS_NOT_ACCEPTED, new MessageHandler<Message>() {
            @Override
            public void handle(Message message) {
                view.alertOrderStateIsNotAccepted();
            }
        });

        addHandler(OrderMessages.TRIAL_ORDER_EXISTS, new MessageHandler<Message>() {
            @Override
            public void handle(Message message) {
                view.alertTrialOrderExists();
            }
        });

        addHandler(OrderMessages.SEARCH, new MessageHandler<SearchOrderMessage>() {
            @Override
            public void handle(SearchOrderMessage message) {
                query = message.getQuery();
                view.reload();
            }
        });

        addHandler(OrderMessages.ORDER_NUMBER_EXISTS, new MessageHandler<Message>() {
            @Override
            public void handle(Message message) {
                view.alertOrderNumberExists();
            }
        });

        addHandler(OrderMessages.GENERATE_PDF_RESULT, new MessageHandler<Message>() {
            @Override
            public void handle(Message message) {
                view.reload();
                view.infoGeneratePdfResult();
            }
        });

        addHandler(OrderMessages.REGENERATE_ORDER_REQUEST_RESULT,
                new MessageHandler<RegenerateOrderRequestResultMessage>() {
                    @Override
                    public void handle(RegenerateOrderRequestResultMessage message) {
                        int orderId = message.getOrderId();
                        send(new RegenerateOrderMessage(orderId));
                    }
                });

        addHandler(OrderMessages.PUBLISH_WEB_FLASH_RESULT, new MessageHandler<Message>() {
            @Override
            public void handle(Message message) {
                view.infoPublishWebFlashResult();
            }
        });
    }

    @Override
    public void exportButtonClicked() {
        service.export(new XAsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
            }
        });
        view.informExportStarted();
    }

    @Override
    public void bulkEditButtonClicked() {
        view.showBulkEditForm(i18nService.getLocale());
    }

    @Override
    public void saveButtonClickedOnBulkEditForm() {
        List<Order<?>> orders = view.getSelectedOrders();
        if (!orders.isEmpty()) {
            view.fetchBulk(orders);
            service.bulkUpdateOrders(orders, new XAsyncCallback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    view.reload();
                    view.hideBulkEditForm();
                }
            });
        }
    }

    @Override
    public void onPublishOrderButtonClicked(final int orderId) {
        service.publishOrder(orderId, new XAsyncCallback<Integer>() {
            @Override
            public void onSuccess(Integer code) {
                goToOrderPublicationPage(code, orderId);
            }

            @Override
            public void onFailure(Throwable caught) {
                view.alertPublishOrderFailed();
            }
        });
    }

    private void goToOrderPublicationPage(Integer code, int orderId) {
        String url = orderService.getOrderPublicationPageUrl(code, orderId, vendorService.getVendor());
        Window.open(url, "_blank", null);
    }
}
