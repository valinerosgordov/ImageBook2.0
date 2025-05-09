package ru.minogin.core.client.browser;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;

public class EscSuppresser {
	public static void suppress() {
		Event.addNativePreviewHandler(new NativePreviewHandler() {
			@Override
			public void onPreviewNativeEvent(NativePreviewEvent event) {
				if (event.getTypeInt() == Event.ONKEYPRESS) {
					if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
						event.cancel();
					}
				}
			}
		});
	}
}
