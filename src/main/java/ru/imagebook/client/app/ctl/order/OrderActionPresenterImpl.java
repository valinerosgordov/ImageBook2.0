package ru.imagebook.client.app.ctl.order;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import ru.imagebook.client.app.service.OrderRemoteServiceAsync;
import ru.imagebook.client.app.util.rpc.AsyncCallback;
import ru.imagebook.client.app.view.order.OrderActionView;
import ru.imagebook.client.common.service.order.OrderService;
import ru.imagebook.client.common.service.vendor.VendorService;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;
import ru.imagebook.shared.model.OrderType;
import ru.imagebook.shared.model.Vendor;
import ru.saasengine.client.service.auth.AuthService;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 * @since 18.02.2015
 */
public class OrderActionPresenterImpl implements OrderActionPresenter {
    private final OrderRemoteServiceAsync orderRemoteService;
    private final OrderService orderService;
    private final VendorService vendorService;
    private final AuthService authService;
    private final OrderActionView view;
    private AlbumService albumService;

    private Map<OrderAction, SuccessCallback> listeners = new HashMap<OrderAction, SuccessCallback>();
    private Order<?> order;

    @AssistedInject
    public OrderActionPresenterImpl(OrderActionView view, VendorService vendorService, AuthService authService, OrderRemoteServiceAsync orderRemoteService, OrderService orderService, @Assisted Order<?> order, AlbumService albumService) {
        this.view = view;
        this.albumService = albumService;
        view.setPresenter(this);
        this.vendorService = vendorService;
        this.authService = authService;
        this.orderRemoteService = orderRemoteService;
        this.orderService = orderService;
        this.order = order;
    }

    @AssistedInject
    public OrderActionPresenterImpl(OrderActionView view, VendorService vendorService, AuthService authService,
                                    OrderRemoteServiceAsync orderRemoteService, OrderService orderService,
                                    @Assisted Order<?> order, @Assisted List<OrderAction> orderActions, AlbumService albumService) {
        this(view, vendorService, authService, orderRemoteService, orderService, order, albumService);

        if (orderActions != null) {
            addOrderActions(orderActions);
        }
    }

    @Override
    public Widget show() {
        return view.asWidget();
    }

    @Override
    public void addOrderAction(OrderAction orderAction) {
        view.addOrderActionButton(orderAction);
    }

    @Override
    public void addOrderActions(List<OrderAction> orderActions) {
        for (OrderAction orderAction : orderActions) {
            addOrderAction(orderAction);
        }
    }

    @Override
    public void onProcessOrderButtonClicked() {
        orderRemoteService.processOnlineOrder(order.getId(), new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                notifyListener(OrderAction.PROCESS);
            }

            @Override
            public void onFailure(Throwable caught) {
                view.alertProcessOrderFailed();
            }
        });
    }

    @Override
    public void onEditOrderButtonClicked() {
        if (order.getType() == OrderType.BOOK && order.getState() == OrderState.NEW) {
            albumService.editAlbum(order.getAlbumId());
            return;
        }

        orderRemoteService.editOrder(order.getId(), new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                notifyListener(OrderAction.EDIT);
                if (order.isEditorOrder()) {
                    goToEditor(order);
                } else if (order.isExternalOrder()) {
                    goToOnlineEditor();
                }
                else if (order.getType() == OrderType.BOOK) {
                    albumService.editAlbum(order.getAlbumId());
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                view.alertEditOrderFailed();
            }
        });
    }

    @Override
    public void onCopyOrderButtonClicked() {
        orderRemoteService.copyOrder(order.getId(), new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                notifyListener(OrderAction.COPY);
            }

            @Override
            public void onFailure(Throwable caught) {
                view.alertCopyOrderFailed();
            }
        });
    }

    @Override
    public void onDeleteOrderButtonClicked() {
        view.showDeleteOrderConfirmation();
    }

    @Override
    public void onDeleteOrderConfirmButtonClicked() {
        orderRemoteService.deleteOrder(order.getId(), new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                view.alertOrderDeleted();
                notifyListener(OrderAction.DELETE);
            }

            @Override
            public void onFailure(Throwable caught) {
                view.alertDeleteOrderFailed();
            }
        });
    }

    @Override
    public void onPublishOrderButtonClicked() {
        orderRemoteService.publishOrder(order.getId(), new AsyncCallback<Integer>() {
            @Override
            public void onSuccess(Integer code) {
                goToOrderPublicationPage(code, order.getId());
            }

            @Override
            public void onFailure(Throwable caught) {
                view.alertPublishOrderFailed();
            }
        });
    }

    private void notifyListener(OrderAction orderAction) {
        SuccessCallback orderActionListener = listeners.get(orderAction);
        if (orderActionListener != null) {
            orderActionListener.onSuccess();
        }
    }

    private void goToEditor(Order<?> order) {
        Vendor vendor = vendorService.getVendor();
        String sessionId = authService.getSessionId();
        String url = "http://" + vendor.getEditorUrl() + "?editOrderId=" + order.getId() + "&sid=" + sessionId;
        Window.open(url, "_blank", null);
    }

    private void goToOnlineEditor() {
        Vendor vendor = vendorService.getVendor();
        Window.Location.assign("http://" + vendor.getOnlineEditorUrl() + "/editor/");
    }

    private void goToOrderPublicationPage(Integer code, int orderId) {
        String url = orderService.getOrderPublicationPageUrl(code, orderId, vendorService.getVendor());
        Window.open(url, "_blank", null);
    }

    @Override
    public void addOrderActionListener(OrderAction orderAction, SuccessCallback callback) {
        this.listeners.put(orderAction, callback);
    }

    @Override
    public void addOrderActionListener(EnumSet<OrderAction> orderActions, SuccessCallback callback) {
        for (OrderAction orderAction : orderActions) {
            addOrderActionListener(orderAction, callback);
        }
    }
}
