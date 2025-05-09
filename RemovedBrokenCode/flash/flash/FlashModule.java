package ru.imagebook.client.flash;

import ru.imagebook.client.flash.view.FlashView;
import ru.imagebook.client.flash.view.FlashViewImpl;
import ru.minogin.core.client.mvp.InjectedPlaceController;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Singleton;

public class FlashModule extends AbstractGinModule {
	@Override
	protected void configure() {
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		bind(PlaceController.class).to(InjectedPlaceController.class);
		bind(FlashView.class).to(FlashViewImpl.class);
	}
}
