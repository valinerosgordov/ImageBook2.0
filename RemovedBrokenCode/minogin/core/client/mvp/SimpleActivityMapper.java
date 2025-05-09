package ru.minogin.core.client.mvp;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

public class SimpleActivityMapper implements ActivityMapper {
	private Map<Place, Activity> activities = new HashMap<Place, Activity>();

	@Override
	public Activity getActivity(Place place) {
		return activities.get(place);
	}

	public void map(Place place, Activity activity) {
		activities.put(place, activity);
	}
}
