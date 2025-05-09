package ru.imagebook.client.flash;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules( { FlashModule.class })
public interface FlashInjector extends Ginjector {
	FlashApp getFlashApp();
}
