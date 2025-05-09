package ru.imagebook.client.app.ctl.payment;

import ru.imagebook.client.app.ctl.AbstractPlace;
import ru.imagebook.client.app.ctl.NameTokens;
import ru.imagebook.shared.model.Bill;


public class PaymentMethodsPlace extends AbstractPlace {
    private Bill bill;

    public PaymentMethodsPlace() {
    }

    public PaymentMethodsPlace(Bill bill) {
        this();
        this.bill = bill;
    }

    public Bill getBill() {
        return bill;
    }

    @Override
    public String getToken() {
        return NameTokens.PAYMENT;
    }
}
