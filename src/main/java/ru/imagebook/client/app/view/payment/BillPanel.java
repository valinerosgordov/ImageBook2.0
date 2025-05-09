package ru.imagebook.client.app.view.payment;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
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
import ru.imagebook.client.common.service.BillCalculator;
import ru.imagebook.client.common.service.I18nService;
import ru.imagebook.client.common.service.vendor.VendorService;
import ru.imagebook.client.common.view.order.OrderConstants;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.VendorType;


public class BillPanel extends BaseBillPanel {
    private final OrderConstants orderConstants;
    private final OrderMessages orderMessages;
    private final OrderViewer orderViewer;
    private final I18nService i18nService;
    private final PlaceController placeController;
    private final VendorService vendorService;
    private final OrderActionPresenterFactory orderActionPresenterFactory;

    @AssistedInject
    public BillPanel(OrderConstants orderConstants, OrderMessages orderMessages,
                     OrderViewer orderViewer, I18nService i18nService, PlaceController placeController,
                     VendorService vendorService, OrderActionPresenterFactory orderActionPresenterFactory,
                     @Assisted Bill bill) {
        super(bill, orderMessages);

        this.orderConstants = orderConstants;
        this.orderMessages = orderMessages;
        this.orderViewer = orderViewer;
        this.i18nService = i18nService;
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
        ordersTable.setText(0, col++, orderConstants.orderId());
        ordersTable.setText(0, col++, orderConstants.order());
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
            ordersTable.setText(row, col++, order.getNumber() + "");

            ordersTable.setText(row, col++, order.getProduct().getName().getNonEmptyValue(i18nService.getLocale()));

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

    // FIXME duplicated code, refactor
    private Widget createOrderActionsPanel(Order<?> order) {
        OrderActionPresenter orderActionPresenter = orderActionPresenterFactory.create(order);

        if (order.isExternalOrder() || order.isOnlineEditorOrder()) {
            orderActionPresenter.addOrderAction(OrderAction.COPY);
            orderActionPresenter.addOrderActionListener(OrderAction.COPY, new OrderActionPresenter.SuccessCallback() {
                @Override
                public void onSuccess() {
                    placeController.goTo(new OrderPlace());
                }
            });
        }

        Vendor vendor = vendorService.getVendor();
        if (vendor.getType() == VendorType.IMAGEBOOK
                && order.getState() >= OrderState.FLASH_GENERATED
                && !OrderState.FLASH_JPEG_GENERATION_STATES.contains(order.getState())
                && order.getState() != OrderState.JPEG_GENERATION_ERROR) {
            orderActionPresenter.addOrderAction(OrderAction.PUBLISH);
        }

        return orderActionPresenter.show();
    }

    @Override
    protected void setBillTotals(HasWidgets totalsPanel, Bill bill) {
        int billDeliveryCost = bill.getDeliveryCost();
        if (billDeliveryCost > 0) {
            totalsPanel.add(new HTML(getDeliveryCostText(bill)));
        }
        int total = BillCalculator.computeTotal(bill);
        totalsPanel.add(new HTML(orderMessages.billTotal(total)));
    }

    private String getDeliveryCostText(Bill bill) {
        int computedDeliveryCost = BillCalculator.computeDeliveryCost(bill);

        return bill.isHasDeliveryDiscount()
            ? orderMessages.billDeliveryDiscountCost(bill.getDeliveryCost(), computedDeliveryCost)
            : orderMessages.billDeliveryCost(bill.getDeliveryCost());
    }
}
