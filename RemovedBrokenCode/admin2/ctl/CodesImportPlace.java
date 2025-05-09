package ru.imagebook.client.admin2.ctl;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class CodesImportPlace extends Place {
	public static class Tokenizer implements PlaceTokenizer<CodesImportPlace> {
		@Override
		public String getToken(CodesImportPlace place) {
			return "";
		}

		@Override
		public CodesImportPlace getPlace(String token) {
			return new CodesImportPlace();
		}
	}
}
