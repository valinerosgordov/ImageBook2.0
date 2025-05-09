package ru.imagebook.client.app.ctl.personal;

import ru.imagebook.client.app.ctl.AbstractPlace;
import ru.imagebook.client.app.ctl.NameTokens;


public class PersonalPlace extends AbstractPlace {
    @Override
    public String getToken() {
        return NameTokens.PERSONAL;
    }
}
