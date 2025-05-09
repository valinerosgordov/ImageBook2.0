package ru.imagebook.client.app.ctl;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;

import ru.imagebook.client.app.ctl.order.OrderActivity;
import ru.imagebook.client.app.ctl.order.OrderPlace;
import ru.imagebook.client.app.ctl.payment.BillsActivity;
import ru.imagebook.client.app.ctl.payment.BillsPlace;
import ru.imagebook.client.app.ctl.payment.DeliveryActivity;
import ru.imagebook.client.app.ctl.payment.DeliveryPlace;
import ru.imagebook.client.app.ctl.payment.PaymentMethodsActivity;
import ru.imagebook.client.app.ctl.payment.PaymentMethodsPlace;
import ru.imagebook.client.app.ctl.personal.PersonalActivity;
import ru.imagebook.client.app.ctl.personal.PersonalPlace;
import ru.imagebook.client.app.ctl.process.ProcessActivity;
import ru.imagebook.client.app.ctl.process.ProcessPlace;
import ru.imagebook.client.app.ctl.sent.SentActivity;
import ru.imagebook.client.app.ctl.sent.SentPlace;
import ru.imagebook.client.app.ctl.support.SupportActivity;
import ru.imagebook.client.app.ctl.support.SupportPlace;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.Order;


public class AppActivityMapper implements ActivityMapper {
    public interface ActivityFactory {
        OrderActivity ordering();
        BillsActivity bills();
        DeliveryActivity delivery(Order<?> order, Bill bill);
        PaymentMethodsActivity paymentMethods(Bill bill);
        ProcessActivity process();
        SentActivity sent();
        PersonalActivity personal();
        SupportActivity support();
    }

    @Inject
    ActivityFactory factory;

    @Override
    public Activity getActivity(Place place) {
        if (place instanceof OrderPlace) {
            return factory.ordering();
        } else if (place instanceof BillsPlace) {
            return factory.bills();
        } else if (place instanceof DeliveryPlace) {
            DeliveryPlace deliveryPlace = (DeliveryPlace) place;
            return factory.delivery(deliveryPlace.getOrder(), deliveryPlace.getBill());
        } else if (place instanceof PaymentMethodsPlace) {
            PaymentMethodsPlace paymentMethodsPlace = (PaymentMethodsPlace) place;
            return factory.paymentMethods(paymentMethodsPlace.getBill());
        } else if (place instanceof ProcessPlace) {
            return factory.process();
        } else if (place instanceof SentPlace) {
            return factory.sent();
        } else if (place instanceof PersonalPlace) {
            return factory.personal();
        } else if (place instanceof SupportPlace) {
            return factory.support();
        } else {
            throw new RuntimeException("Unknown place: " + place);
        }
    }
}
