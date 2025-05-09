package ru.imagebook.client.admin2.ctl;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;


public class BannersPlace extends Place {
    public static class Tokenizer implements PlaceTokenizer<BannersPlace> {
        @Override
        public String getToken(BannersPlace place) {
            return "";
        }

        @Override
        public BannersPlace getPlace(String token) {
            return new BannersPlace();
        }
    }
}
