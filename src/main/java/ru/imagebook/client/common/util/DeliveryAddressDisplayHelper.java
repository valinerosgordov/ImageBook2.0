package ru.imagebook.client.common.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.app.view.CompilerFactory;
import ru.imagebook.client.app.view.payment.DeliveryConstants;
import ru.imagebook.shared.util.delivery.impl.AbstractDeliveryAddressHelper;
import ru.minogin.core.client.lang.template.Compiler;

@Singleton
public class DeliveryAddressDisplayHelper extends AbstractDeliveryAddressHelper {

    @Inject
    private DeliveryConstants deliveryConstants;

    @Inject
    private CompilerFactory compilerFactory;

    @Override
    protected String getAddressTemplate() {
        return deliveryConstants.addressTemplate();
    }

    @Override
    protected Compiler getCompiler() {
        return compilerFactory.getCompiler();
    }
}
