package ru.imagebook.client.flash;

import ru.imagebook.client.flash.ctl.FlashActivity;
import ru.imagebook.client.flash.ctl.FlashPlace;
import ru.minogin.core.client.mvp.SimpleActivityMapper;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;

public class FlashApp {
	private SimpleActivityMapper activityMapper = new SimpleActivityMapper();
	@Inject
	private EventBus eventBus;
	@Inject
	private PlaceController placeController;
	@Inject
	private FlashActivity flashActivity;

	public void start() {
		activityMapper.map(new FlashPlace(), flashActivity);
		ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
		RootPanel rootPanel = RootPanel.get("app");
		rootPanel.clear();
		SimplePanel panel = new SimplePanel();
		rootPanel.add(panel);
		activityManager.setDisplay(panel);
		placeController.goTo(new FlashPlace());
	}
}
