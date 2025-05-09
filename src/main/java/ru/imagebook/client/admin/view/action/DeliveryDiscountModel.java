package ru.imagebook.client.admin.view.action;

import ru.imagebook.shared.model.DeliveryDiscount;
import ru.minogin.core.client.gxt.BaseEntityModel;

/**
 * Created by rifat on 19.01.17.
 */
class DeliveryDiscountModel extends BaseEntityModel<DeliveryDiscount> {
    DeliveryDiscountModel(DeliveryDiscount deliveryDiscount) {
        super(deliveryDiscount);
        set(DeliveryDiscount.SUM, deliveryDiscount.getSum());
        set(DeliveryDiscount.DISCOUNT_PC, deliveryDiscount.getDiscountPc());
    }

    int getSum() {
        return get(DeliveryDiscount.SUM);
    }

    int getDiscountPc() {
        return get(DeliveryDiscount.DISCOUNT_PC);
    }
}
