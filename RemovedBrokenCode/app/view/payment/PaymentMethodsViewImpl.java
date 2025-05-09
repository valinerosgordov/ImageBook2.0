package ru.imagebook.client.app.view.payment;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.app.ctl.payment.PaymentMethodsPresenter;
import ru.imagebook.client.app.view.common.Notify;
import ru.imagebook.client.app.view.order.OrderBundle;
import ru.imagebook.client.common.view.order.OrderConstants;
import ru.imagebook.shared.model.Vendor;


@Singleton
public class PaymentMethodsViewImpl implements PaymentMethodsView {
    interface PaymentMethodsUiBinder extends UiBinder<Widget, PaymentMethodsViewImpl> {
    }
    private static PaymentMethodsUiBinder uiBinder = GWT.create(PaymentMethodsUiBinder.class);

    @UiField
    HTMLPanel paymentMethodsPanel;
    @UiField
    Anchor yandexKassaMethodAnchor;
    @UiField
    Anchor roboMethodAnchor;
    @UiField
    Anchor receiptMethodAnchor;
    @UiField
    Anchor unblockPopupsHelpAnchor;

    @UiField
    HTMLPanel receiptMethodForm;
    @UiField
    TextBox nameField;
    @UiField
    TextBox addressField;
    @UiField
    Anchor receiptPdfAnchor;
    @UiField
    Anchor receiptMsWordAnchor;

    private final OrderConstants orderConstants;
    private final OrderBundle bundle;

    private PaymentMethodsPresenter presenter;

    @Inject
    public PaymentMethodsViewImpl(OrderConstants orderConstants, OrderBundle bundle) {
        this.orderConstants = orderConstants;
        this.bundle = bundle;
    }

    @Override
    public void setPresenter(PaymentMethodsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Widget asWidget() {
        return uiBinder.createAndBindUi(this);
    }

    @Override
    public void showPaymentMethods(Vendor vendor) {
        paymentMethodsPanel.setVisible(true);
        receiptMethodForm.setVisible(false);
        roboMethodAnchor.setVisible(vendor.getRoboLogin() != null);
//        yandexKassaMethodAnchor.setVisible(vendor.getYandexShopId() != null && vendor.getYandexScid() != null);
        yandexKassaMethodAnchor.setVisible(false); // ToDo: не уверен
        receiptMethodAnchor.setVisible(false);
    }

    @UiHandler("roboMethodAnchor")
    public void onRoboMethodAnchorClicked(ClickEvent event) {
        presenter.onRoboMethodAnchorClicked();
    }

    @UiHandler("yandexKassaMethodAnchor")
    public void onYandexKassaMethodAnchorClicked(ClickEvent event) {
        presenter.onYandexKassaMethodAnchorClicked();
    }

    @UiHandler("receiptMethodAnchor")
    public void onReceiptMethodAnchorClicked(ClickEvent event) {
        paymentMethodsPanel.setVisible(false);
        receiptMethodForm.setVisible(true);
    }

    @UiHandler("receiptPdfAnchor")
    public void onReceiptPdfAnchorClicked(ClickEvent event) {
        presenter.generateReceiptPdf(nameField.getValue(), addressField.getValue());
    }

    @UiHandler("receiptMsWordAnchor")
    public void onReceiptMSWordAnchorClicked(ClickEvent event) {
        presenter.generateReceiptMSWord(nameField.getValue(), addressField.getValue());
    }

    @UiHandler("unblockPopupsHelpAnchor")
    public void onUnblockPopupsHelpAnchorClicked(ClickEvent event) {
        Notify.notify(orderConstants.anchorHintTitle(), bundle.anchorHint().getText());
    }
}
