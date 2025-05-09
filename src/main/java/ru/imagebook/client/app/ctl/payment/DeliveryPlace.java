package ru.imagebook.client.app.ctl.payment;

import ru.imagebook.client.app.ctl.AbstractPlace;
import ru.imagebook.client.app.ctl.NameTokens;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.Order;


public class DeliveryPlace extends AbstractPlace {
    private Order<?> order;
    private Bill bill;

    public DeliveryPlace() {
    }

    public DeliveryPlace(Order<?> order) {
        this.order = order;
    }

    public DeliveryPlace(Bill bill) {
        this.bill = bill;
    }

    public Order<?> getOrder() {
        return order;
    }

    public Bill getBill() {
        return bill;
    }

    public String getToken() {
        return NameTokens.PAYMENT;
    }
}
