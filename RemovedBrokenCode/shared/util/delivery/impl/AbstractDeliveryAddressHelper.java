package ru.imagebook.shared.util.delivery.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;

import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.DeliveryType;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.util.delivery.DeliveryAddressHelper;
import ru.minogin.core.client.lang.template.Compiler;

public abstract class AbstractDeliveryAddressHelper implements DeliveryAddressHelper {

    protected AbstractDeliveryAddressHelper() {
    }

    @Override
    public String getAddressString(Bill bill) {
        Preconditions.checkNotNull(bill);

        Integer deliveryType = bill.getDeliveryType();
        if (deliveryType == null || bill.getOrders().isEmpty()) {
            return null;
        }

        if (deliveryType == DeliveryType.EXW) { // pickup
            return null;
        }

        String address = null;
        Order<?> firstOrder = Iterables.getFirst(bill.getOrders(), null);

        if (deliveryType == DeliveryType.POSTAMATE) {
            // For POSTAMATEs delivery address stored in order.deliveryComment
            address = firstOrder.getDeliveryComment();
        } else if (firstOrder.getAddress() != null) {
            address = getCompiler().compile(getAddressTemplate(), firstOrder.getAddress());
        }

        return address;
    }

    @Override
    public Address getAddress(Bill bill) {
        Order<?> firstOrder = Iterables.getFirst(bill.getOrders(), null);
        return firstOrder.getAddress();
    }

    protected abstract String getAddressTemplate();

    protected abstract Compiler getCompiler();
}