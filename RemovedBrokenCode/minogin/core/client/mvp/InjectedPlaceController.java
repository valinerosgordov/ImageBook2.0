package ru.minogin.core.client.mvp;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class InjectedPlaceController extends PlaceController {
	@Inject
	public InjectedPlaceController(EventBus eventBus) {
		super(eventBus);
	}
}
