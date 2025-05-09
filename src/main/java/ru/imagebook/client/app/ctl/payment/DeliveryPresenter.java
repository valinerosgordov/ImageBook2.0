package ru.imagebook.client.app.ctl.payment;

import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.DeliveryTypeInfo;
import ru.imagebook.shared.model.app.SDEKCourierData;
import ru.imagebook.shared.model.app.SDEKPickupData;


public interface DeliveryPresenter {
    void onPayButtonClick();

    void initMajorCityField();

    void onDeliveryMethodSelected(DeliveryTypeInfo deliveryTypeInfo);

    void onDeliveryAddressSelected(Address address);

    void onMajorCitySelecting();

    void onMajorCitySelected(String majorCityName);

    void onPickpointPostamateSelected();

    void onSDEKPickupSelected(SDEKPickupData pickupData);

    void onSDEKCourierSelected(SDEKCourierData courierData);

    int computeDeliveryDiscountPc(int DeliveryType);
}
