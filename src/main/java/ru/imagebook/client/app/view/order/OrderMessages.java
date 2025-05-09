package ru.imagebook.client.app.view.order;

import java.util.Date;

import com.google.gwt.i18n.client.Messages;


public interface OrderMessages extends Messages {
	String bill(String number, Date date);

    String billDeliveryMethod(String deliveryMethod);

    String billDeliveryAddress(String address);

    String billDeliveryCost(int value);

	String billTotal(int total);

	String billDeliveryDiscountCost(int cost, int discountedCost);
}
