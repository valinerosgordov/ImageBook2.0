package ru.minogin.core.client.app;

import ru.minogin.core.client.common.Callback;
import ru.minogin.core.client.mvp.SimpleActivityMapper;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public abstract class ApplicationController {
	private EventBus eventBus = new SimpleEventBus();
	private PlaceController placeController = new PlaceController(eventBus);
	private SimpleActivityMapper activityMapper = new SimpleActivityMapper();

	public PlaceController getPlaceController() {
		return placeController;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public final void run() {
		loadApplicationState(new Callback() {
			@Override
			public void onSuccess() {
				mapActivities();
				AcceptsOneWidget display = createDisplay();

				ActivityManager activityManager = new ActivityManager(activityMapper,
						eventBus);
				activityManager.setDisplay(display);

				start();
			}
		});
	}

	protected void loadApplicationState(Callback callback) {
		callback.onSuccess();
	}

	protected abstract void mapActivities();

	protected abstract AcceptsOneWidget createDisplay();

	protected abstract void start();

	protected void map(Place place, Activity activity) {
		activityMapper.map(place, activity);
	}
}
