package ru.imagebook.client.admin2.ctl;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;


public class ExportEmailPlace extends Place {
    public static class Tokenizer implements PlaceTokenizer<ExportEmailPlace> {
        @Override
        public String getToken(ExportEmailPlace place) {
            return "";
        }

        @Override
        public ExportEmailPlace getPlace(String token) {
            return new ExportEmailPlace();
        }
    }
}
