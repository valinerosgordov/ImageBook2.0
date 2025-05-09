package ru.imagebook.client.app.ctl.order;

import ru.imagebook.client.app.ctl.AbstractPlace;
import ru.imagebook.client.app.ctl.NameTokens;


public class OrderPlace extends AbstractPlace {
    @Override
    public String getToken() {
        return NameTokens.ORDER;
    }
}
