package ru.imagebook.client.admin2.ctl;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Created by zinchenko on 08.09.14.
 */
public class FeedbacksPlace extends Place {
    public static class Tokenizer implements PlaceTokenizer<FeedbacksPlace> {
        @Override
        public String getToken(FeedbacksPlace place) {
            return "";
        }

        @Override
        public FeedbacksPlace getPlace(String token) {
            return new FeedbacksPlace();
        }
    }
}
