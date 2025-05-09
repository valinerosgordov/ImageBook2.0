package ru.imagebook.client.app.view.payment;

import com.google.gwt.i18n.client.Messages;


public interface DeliveryConstants extends Messages {
    String deliveryServiceNotAvailable();

    String majorCostAndTimeText(int cost, int time);

    String majorDiscountedCostAndTimeText(int cost, int discountedCost, int time);

    String emailWrong();

    String pickpointCostField(int cost);

    String pickpointDiscountedCostField(int cost, int discountedCost);

    String pickpointTimeFieldSingleValue(int time);

    String pickpointTimeFieldRange(int timeMin, int timeMax);

    String pickpointPostamateUnavailable();

    String postamateId();

    String postameteAddress();

    String addressTemplate();

    String free();

    String sdekDeliveryWhomSectionLabelCourier();

    String sdekDeliveryWhomSectionLabelPickup();
}
