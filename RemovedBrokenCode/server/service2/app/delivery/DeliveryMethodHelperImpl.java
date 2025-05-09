package ru.imagebook.server.service2.app.delivery;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import ru.imagebook.shared.util.delivery.impl.AbstractDeliveryMethodHelper;
import ru.minogin.core.client.i18n.locale.Locales;


@Component
public class DeliveryMethodHelperImpl extends AbstractDeliveryMethodHelper {

    @Autowired
    private MessageSource messages;

    public DeliveryMethodHelperImpl() {
    }

    @Override
    protected String getDeliveryPropertyValue(String lookupCode) {
        return messages.getMessage(lookupCode, null, new Locale(Locales.RU));
    }
}
