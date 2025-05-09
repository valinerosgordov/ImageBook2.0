package ru.imagebook.client.app.view.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

import ru.imagebook.client.app.view.order.OrderMessages;
import ru.imagebook.client.common.service.BillCalculator;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.DeliveryType;
import ru.imagebook.shared.util.delivery.DeliveryAddressHelper;
import ru.imagebook.shared.util.delivery.DeliveryMethodHelper;


public abstract class BaseBillPanel extends Composite {
    interface BillTableUiBinder extends UiBinder<Widget, BaseBillPanel> {
    }

    @UiField
    InlineLabel billTitleLabel;
    @UiField
    FlexTable ordersTable;
    @UiField
    HTMLPanel billTotalPanel;

    private static BillTableUiBinder uiBinder = GWT.create(BillTableUiBinder.class);

    private OrderMessages orderMessages;
    private DeliveryMethodHelper deliveryMethodHelper;
    private DeliveryAddressHelper deliveryAddressHelper;

    private Bill bill;

    public BaseBillPanel(Bill bill, OrderMessages orderMessages) {
        this.bill = bill;
        this.orderMessages = orderMessages;
    }

    public BaseBillPanel(Bill bill, OrderMessages orderMessages, DeliveryMethodHelper deliveryMethodHelper,
                         DeliveryAddressHelper deliveryAddressHelper) {
        this(bill, orderMessages);
        this.deliveryMethodHelper = deliveryMethodHelper;
        this.deliveryAddressHelper = deliveryAddressHelper;
    }

    protected void initWidget() {
        Widget ui = uiBinder.createAndBindUi(this);

        setBillTitle(bill);
        setBillOrders(ordersTable, bill);
        setBillTotals(billTotalPanel, bill);

        initWidget(ui);
    }

    protected void setBillTitle(Bill bill) {
        billTitleLabel.setText(orderMessages.bill(bill.getId() + "", bill.getDate()));
    }

    /**
     * Fills the Bill's orders table
     *
     * @param ordersTable
     * @param bill
     * @return bill total value
     */
    public abstract void setBillOrders(final FlexTable ordersTable, Bill bill);

    protected void setBillTotals(final HasWidgets totalsPanel, Bill bill) {
        Integer deliveryType = bill.getDeliveryType();
        if (deliveryType != null) {
            String deliveryText = orderMessages.billDeliveryMethod(deliveryMethodHelper.getDeliveryMethod(bill));
            totalsPanel.add(new HTML(deliveryText));

            String addressText = deliveryAddressHelper.getAddressString(bill);
            if (deliveryType != DeliveryType.EXW && addressText != null) {
                addressText = orderMessages.billDeliveryAddress(addressText);
                totalsPanel.add(new HTML(addressText));
            }

            String deliveryCostText = orderMessages.billDeliveryCost(BillCalculator.computeDeliveryCost(bill));
            totalsPanel.add(new HTML(deliveryCostText));
        }

        int total = BillCalculator.computeTotal(bill);
        totalsPanel.add(new HTML(orderMessages.billTotal(total)));
    }
}
