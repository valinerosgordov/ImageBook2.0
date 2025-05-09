package ru.imagebook.client.admin.ctl.site;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class SitePlace extends Place {
	public static class Tokenizer implements PlaceTokenizer<SitePlace> {
		@Override
		public SitePlace getPlace(String token) {
			return new SitePlace();
		}

		@Override
		public String getToken(SitePlace place) {
			return null;
		}
	}
}
