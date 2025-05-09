package ru.imagebook.client.admin.view.action;

import ru.imagebook.shared.model.DeliveryDiscount;

/**
 * Created by rifat on 18.01.17.
 */
public interface ActionPresenter {

    void addDeliveryDiscount(DeliveryDiscount deliveryDiscount);

    void updateDeliveryDiscount(DeliveryDiscount deliveryDiscount);

    void showDeliverySection();

    void deleteDeliveryDiscount(Integer id);
}
