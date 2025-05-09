package ru.imagebook.server.repository;

import java.util.List;
import java.util.Map;

import ru.imagebook.shared.model.DeliveryDiscount;

/**
 * Created by rifat on 19.01.17.
 */
public interface DeliveryDiscountRepository {
    Integer addDeliveryDiscount(DeliveryDiscount deliveryDiscount);

    void updateDeliveryDiscount(DeliveryDiscount deliveryDiscount);

    List<DeliveryDiscount> loadDeliveryDiscounts();

    void deleteDeliveryDiscount(Integer id);

    Map<Integer, DeliveryDiscount> getDiscounts(int sum);
}
