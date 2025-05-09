package ru.imagebook.client.app.view.payment;

import com.google.gwt.user.client.ui.IsWidget;

import ru.imagebook.client.app.ctl.payment.PaymentMethodsPresenter;
import ru.imagebook.shared.model.Vendor;


public interface PaymentMethodsView extends IsWidget {
    void setPresenter(PaymentMethodsPresenter presenter);

    void showPaymentMethods(Vendor vendor);
}
