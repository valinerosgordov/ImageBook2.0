package ru.imagebook.client.app.ctl.support;

import ru.imagebook.client.app.ctl.AbstractPlace;
import ru.imagebook.client.app.ctl.NameTokens;


public class SupportPlace extends AbstractPlace {
    @Override
    public String getToken() {
        return NameTokens.SUPPORT;
    }
}
