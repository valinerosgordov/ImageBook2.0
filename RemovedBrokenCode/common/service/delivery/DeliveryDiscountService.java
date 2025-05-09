package ru.imagebook.client.common.service.delivery;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import ru.imagebook.shared.model.DeliveryDiscount;
import ru.imagebook.shared.service.admin.delivery.DeliveryDiscountExistsException;

/**
 * Created by rifat on 19.01.17.
 */
@RemoteServiceRelativePath("deliveryDiscount.remoteService")
public interface DeliveryDiscountService extends RemoteService {
    Integer addDeliveryDiscount(DeliveryDiscount deliveryDiscount) throws DeliveryDiscountExistsException;

    void updateDeliveryDiscount(DeliveryDiscount deliveryDiscount) throws DeliveryDiscountExistsException;

    List<DeliveryDiscount> loadDeliveryDiscounts();

    Map<Integer, DeliveryDiscount> getDiscounts(int sum);

    void deleteDeliveryDiscount(Integer id);
}
