package ru.imagebook.client.common.service.delivery;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

import ru.imagebook.shared.model.DeliveryDiscount;
import ru.imagebook.shared.service.admin.delivery.DeliveryDiscountExistsException;

/**
 * Created by rifat on 19.01.17.
 */
public interface DeliveryDiscountServiceAsync {
    void addDeliveryDiscount(DeliveryDiscount deliveryDiscount, AsyncCallback<Integer> callback)
        throws DeliveryDiscountExistsException;

    void updateDeliveryDiscount(DeliveryDiscount deliveryDiscount, AsyncCallback<Void> callback)
        throws DeliveryDiscountExistsException;

    void loadDeliveryDiscounts(AsyncCallback<List<DeliveryDiscount>> callback);

    void getDiscounts(int sum, AsyncCallback<Map<Integer, DeliveryDiscount>> callback);

    void deleteDeliveryDiscount(Integer id, AsyncCallback<Void> callback);
}
