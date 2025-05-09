package ru.imagebook.shared.util.delivery.impl;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;

import static ru.imagebook.client.common.service.delivery.PostHouseType.FIRST_CLASS;
import static ru.imagebook.client.common.service.delivery.PostHouseType.NORMAL;
import ru.imagebook.client.common.service.delivery.PostHouseType;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.DeliveryType;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.util.delivery.DeliveryMethodHelper;

public abstract class AbstractDeliveryMethodHelper implements DeliveryMethodHelper {
    public static final String DELIVERY_TYPE_NAME_PROPERTY = "deliveryTypeName";
    public static final String DELIVERY_COMMENT_PROPERTY = "deliveryTypeComment";

    protected AbstractDeliveryMethodHelper() {
    }

    @Override
    public String getDeliveryMethod(Bill bill) {
        Preconditions.checkNotNull(bill);

        Integer deliveryType = bill.getDeliveryType();
        if (deliveryType == null || bill.getOrders().isEmpty()) {
            return null;
        }

        String deliveryMethod;

        if (deliveryType == DeliveryType.POST) {
            PostHouseType postHouseType = isPostFirstClassDelivery(bill) ? FIRST_CLASS : NORMAL;
            deliveryMethod = getDeliveryMethodLabel(DeliveryType.POST, postHouseType);
        } else {
            deliveryMethod = getDeliveryMethodLabel(deliveryType, null);
        }

        return deliveryMethod;
    }

    private boolean isPostFirstClassDelivery(Bill bill) {
        // use first order because detail delivery information is stored and duplicated in bill's orders
        Order<?> firstOrder = Iterables.getFirst(bill.getOrders(), null);
        // a little magic - identifying subtype of deliveryType.POST by comment for delivery
        String firstClassComment = getDeliveryCommentLabel(DeliveryType.POST, PostHouseType.FIRST_CLASS);
        return firstClassComment != null && firstClassComment.equals(firstOrder.getDeliveryComment());
    }

    private String getDeliveryMethodLabel(Integer deliveryType, PostHouseType postHouseType) {
        String lookupCode = DELIVERY_TYPE_NAME_PROPERTY + deliveryType + Objects.firstNonNull(postHouseType, "");
        return getDeliveryPropertyValue(lookupCode);
    }

    private String getDeliveryCommentLabel(Integer deliveryType, PostHouseType postHouseType) {
        String lookupCode = DELIVERY_COMMENT_PROPERTY + deliveryType + Objects.firstNonNull(postHouseType, "");
        return getDeliveryPropertyValue(lookupCode);
    }

    protected abstract String getDeliveryPropertyValue(String lookupCode);
}
