package ru.minogin.core.client.browser;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;

public class Browser {
	public static native void refresh() /*-{
	 $wnd.location.reload();
	}-*/;

	public static native void alert(String message) /*-{
	  alert(message);
	}-*/;

	public static native void goTo(String url) /*-{
		$wnd.location.href = url;
	}-*/;

	public static void setCloseConfirmation(final String message) {
		Window.addWindowClosingHandler(new ClosingHandler() {
			@Override
			public void onWindowClosing(ClosingEvent event) {
				event.setMessage(message);
			}
		});
	}
}
