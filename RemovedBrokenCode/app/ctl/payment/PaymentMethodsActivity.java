package ru.imagebook.client.app.ctl.payment;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import ru.imagebook.client.app.view.payment.PaymentMethodsView;
import ru.imagebook.client.common.service.order.Format;
import ru.imagebook.client.common.service.vendor.VendorService;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.Vendor;


public class PaymentMethodsActivity extends AbstractActivity implements PaymentMethodsPresenter {
    private final PaymentMethodsView view;
    private final VendorService vendorService;

    private Bill bill;

    @AssistedInject
    public PaymentMethodsActivity(PaymentMethodsView view, VendorService vendorService, @Assisted Bill bill) {
        this.view = view;
        view.setPresenter(this);
        this.vendorService = vendorService;
        this.bill = bill;
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        panel.setWidget(view);
        view.showPaymentMethods(vendorService.getVendor());
    }

    @Override
    public void onRoboMethodAnchorClicked() {
        String url = GWT.getHostPageBaseURL() + "robo?billId=" + bill.getId();
        Window.open(url, "_blank", null);
    }

    @Override
    public void onYandexKassaMethodAnchorClicked() {
        Vendor vendor = vendorService.getVendor();
        String url = "http://" + vendor.getSite() + "/ya_kassa/pay?billId=" + bill.getId();
        Window.open(url, "_blank", null);
    }

    @Override
    public void generateReceiptPdf(String name, String address) {
        generateReceipt(Format.PDF, name, address);
    }

    @Override
    public void generateReceiptMSWord(String name, String address) {
        generateReceipt(Format.DOC, name, address);
    }

    private void generateReceipt(int format, String name, String address) {
        String url = GWT.getHostPageBaseURL() + "generateReceipt?billId=" + bill.getId()
            + "&format=" + format + "&name=" + name + "&address=" + address;
        Window.open(url, null, null);
    }
}
