package ru.imagebook.server.service2.app.delivery;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import ru.imagebook.shared.util.delivery.impl.AbstractDeliveryAddressHelper;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.i18n.lang.ImplodeFunction;
import ru.minogin.core.client.i18n.lang.PrefixFunction;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.client.lang.template.Compiler;


@Component
public class DeliveryAddressHelperImpl extends AbstractDeliveryAddressHelper {

    @Autowired
    private CoreFactory coreFactory;

    @Autowired
    private MessageSource messages;

    @Override
    protected String getAddressTemplate() {
        return messages.getMessage("deliveryAddressTemplate", null, new Locale(Locales.RU));
    }

    @Override
    protected Compiler getCompiler() {
        Compiler compiler = coreFactory.createCompiler();
        compiler.registerFunction("implode", new ImplodeFunction());
        compiler.registerFunction("prefix", new PrefixFunction());
        return compiler;
    }
}
