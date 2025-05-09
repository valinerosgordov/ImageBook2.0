package ru.imagebook.client.admin2.ctl;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Created by zinchenko on 08.09.14.
 */
public class RecommendationsPlace extends Place {
    public static class Tokenizer implements PlaceTokenizer<RecommendationsPlace> {
        @Override
        public String getToken(RecommendationsPlace place) {
            return "";
        }

        @Override
        public RecommendationsPlace getPlace(String token) {
            return new RecommendationsPlace();
        }
    }
}
