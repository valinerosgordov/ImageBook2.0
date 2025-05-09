package ru.imagebook.client.app.ctl;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;

import ru.imagebook.client.app.ctl.order.OrderPlace;
import ru.imagebook.client.app.ctl.payment.BillsPlace;
import ru.imagebook.client.app.ctl.personal.PersonalPlace;
import ru.imagebook.client.app.ctl.process.ProcessPlace;
import ru.imagebook.client.app.ctl.sent.SentPlace;
import ru.imagebook.client.app.ctl.support.SupportPlace;


public class AppPlaceHistoryMapper implements PlaceHistoryMapper {
    private static final Map<String, Place> TOKEN_TO_PLACE_MAP = new ImmutableMap.Builder<String, Place>()
        .put(NameTokens.ORDER, new OrderPlace())
        .put(NameTokens.PAYMENT, new BillsPlace())
        .put(NameTokens.PROCESS, new ProcessPlace())
        .put(NameTokens.SENT, new SentPlace())
        .put(NameTokens.PERSONAL, new PersonalPlace())
        .put(NameTokens.SUPPORT, new SupportPlace())
        .build();

    @Override
    public Place getPlace(String token) {
        return TOKEN_TO_PLACE_MAP.get(token);
    }

    @Override
    public String getToken(Place place) {
        if (place instanceof AbstractPlace) {
            return ((AbstractPlace) place).getToken();
        }
        return null;
    }
}
