package ru.imagebook.client.app.ctl.sent;

import ru.imagebook.client.app.ctl.AbstractPlace;
import ru.imagebook.client.app.ctl.NameTokens;


public class SentPlace extends AbstractPlace {
    @Override
    public String getToken() {
        return NameTokens.SENT;
    }
}
