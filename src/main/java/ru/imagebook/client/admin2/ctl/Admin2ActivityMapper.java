package ru.imagebook.client.admin2.ctl;

import ru.imagebook.client.admin2.view.BannersViewImpl;
import ru.imagebook.client.admin2.view.CodesImportViewImpl;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;

import ru.imagebook.client.admin2.view.ExportEmailViewImpl;
import ru.imagebook.client.admin2.view.FeedbacksViewImpl;
import ru.imagebook.client.admin2.view.RecommendationsViewImpl;

public class Admin2ActivityMapper implements ActivityMapper {
	private final PlaceController placeController;

	public Admin2ActivityMapper(PlaceController placeController) {
		this.placeController = placeController;
	}

	@Override
	public Activity getActivity(Place place) {
		if (place instanceof CodesImportPlace) {
			return new CodesImportActivity(placeController, new CodesImportViewImpl());
		} else if (place instanceof FeedbacksPlace) {
			return new FeedbacksActivity(placeController, new FeedbacksViewImpl());
		} else if (place instanceof RecommendationsPlace) {
			return new RecommendationsActivity(placeController, new RecommendationsViewImpl());
		} else if (place instanceof BannersPlace) {
            return new BannersActivity(placeController, new BannersViewImpl());
        } else if (place instanceof ExportEmailPlace) {
            return new ExportEmailActivity(placeController, new ExportEmailViewImpl());
		} else {
			throw new RuntimeException("Unknown place: " + place);
		}
	}
}
