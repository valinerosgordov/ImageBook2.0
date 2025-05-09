package ru.imagebook.client.app.ctl.payment;

import ru.imagebook.client.app.ctl.AbstractPlace;
import ru.imagebook.client.app.ctl.NameTokens;


public class BillsPlace extends AbstractPlace {
    @Override
    public String getToken() {
        return NameTokens.PAYMENT;
    }
}
