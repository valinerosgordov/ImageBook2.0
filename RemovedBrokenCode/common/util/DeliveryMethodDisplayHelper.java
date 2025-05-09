package ru.imagebook.client.common.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.app.view.payment.DeliveryConstantsWithLookup;
import ru.imagebook.shared.util.delivery.impl.AbstractDeliveryMethodHelper;


@Singleton
public class DeliveryMethodDisplayHelper extends AbstractDeliveryMethodHelper {

    @Inject
    private DeliveryConstantsWithLookup constantsWithLookup;

    @Override
    protected String getDeliveryPropertyValue(String lookupCode) {
        return constantsWithLookup.getString(lookupCode);
    }
}
