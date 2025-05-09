package ru.imagebook.client.app.view.process;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import ru.imagebook.client.app.ctl.order.OrderAction;
import ru.imagebook.client.app.ctl.order.OrderActionPresenter;
import ru.imagebook.client.app.ctl.order.OrderActionPresenterFactory;
import ru.imagebook.client.app.ctl.order.OrderPlace;
import ru.imagebook.client.app.view.common.BaseBillPanel;
import ru.imagebook.client.app.view.common.OrderViewer;
import ru.imagebook.client.app.view.order.OrderMessages;
import ru.imagebook.client.common.service.I18nService;
import ru.imagebook.client.common.service.vendor.VendorService;
import ru.imagebook.client.common.view.order.OrderConstants;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.VendorType;
import ru.imagebook.shared.util.delivery.DeliveryAddressHelper;
import ru.imagebook.shared.util.delivery.DeliveryMethodHelper;
import ru.minogin.core.client.gwt.DateFormat;


public class BillPanel extends BaseBillPanel {
    private final OrderConstants orderConstants;
    private final I18nService i18nService;
    private final OrderViewer orderViewer;
    private final PlaceController placeController;
    private final VendorService vendorService;
    private final OrderActionPresenterFactory orderActionPresenterFactory;

    @AssistedInject
    public BillPanel(OrderConstants orderConstants, OrderMessages orderMessages, I18nService i18nService,
                     OrderViewer orderViewer, PlaceController placeController,
                     OrderActionPresenterFactory orderActionPresenterFactory, VendorService vendorService,
                     DeliveryMethodHelper deliveryMethodHelper, DeliveryAddressHelper deliveryAddressHelper,
                     @Assisted Bill bill) {

        super(bill, orderMessages, deliveryMethodHelper, deliveryAddressHelper);

        this.orderConstants = orderConstants;
        this.i18nService = i18nService;
        this.orderViewer = orderViewer;
        this.placeController = placeController;
        this.vendorService = vendorService;
        this.orderActionPresenterFactory = orderActionPresenterFactory;

        initWidget();
    }

    @Override
    public void setBillOrders(FlexTable ordersTable, Bill bill) {
        // headers
        int col = 0;
        ordersTable.setText(0, col++, orderConstants.flash());
        ordersTable.setText(0, col++, orderConstants.date());
        ordersTable.setText(0, col++, orderConstants.number());
        ordersTable.setText(0, col++, orderConstants.order());
        ordersTable.setText(0, col++, orderConstants.state());
        ordersTable.setText(0, col++, orderConstants.price());
        ordersTable.setText(0, col++, orderConstants.quantity());
        ordersTable.setText(0, col++, orderConstants.discount());
        ordersTable.setText(0, col++, orderConstants.cost());
        ordersTable.setText(0, col, orderConstants.action());

        for (int i = 0; i <= col; i++) {
            ordersTable.getCellFormatter().addStyleName(0, i, "th");
        }

        // data
        int row = 1;
        for (final Order<?> order : bill.getOrders()) {
            col = 0;

            ordersTable.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
            ordersTable.setWidget(row, col, orderViewer.createOrderPreviewWidget(order));
            col++;

            ordersTable.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_RIGHT);
            ordersTable.setText(row, col++, DateFormat.formatDate(order.getDate()));

            ordersTable.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_RIGHT);
            ordersTable.setText(row, col++, order.getNumber() + "");

            String locale = i18nService.getLocale();
            ordersTable.setText(row, col++, order.getProduct().getName().getNonEmptyValue(locale));

            int state = order.getState();
            String stateText = OrderState.values.get(state).get(locale);
            ordersTable.setText(row, col++, stateText);

            ordersTable.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_RIGHT);
            ordersTable.setText(row, col++, order.getPrice() + "");

            ordersTable.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_RIGHT);
            ordersTable.setText(row, col++, order.getQuantity() + "");

            ordersTable.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_RIGHT);
            ordersTable.setText(row, col++, order.getDiscount() + "");

            ordersTable.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_RIGHT);
            Integer cost = order.getCost();
            ordersTable.setText(row, col++, cost + "");


            ordersTable.setWidget(row, col, createOrderActionsPanel(order));

            row++;
        }
    }

    private Widget createOrderActionsPanel(Order<?> order) {
        OrderActionPresenter orderActionPresenter = orderActionPresenterFactory.create(order);

        if (order.isExternalOrder()) {
            orderActionPresenter.addOrderAction(OrderAction.COPY);
            orderActionPresenter.addOrderActionListener(OrderAction.COPY, new OrderActionPresenter.SuccessCallback() {
                @Override
                public void onSuccess() {
                    placeController.goTo(new OrderPlace());
                }
            });
        }

        Vendor vendor = vendorService.getVendor();
        if (vendor.getType() == VendorType.IMAGEBOOK) {
            orderActionPresenter.addOrderAction(OrderAction.PUBLISH);
        }

        return orderActionPresenter.show();
    }
}
