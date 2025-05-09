package ru.minogin.gwt.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;

public abstract class XAsyncCallback<T> implements AsyncCallback<T> {
	private static RpcFailureHandler handler;
	private static boolean showPopup = false;

	public static void setHandler(RpcFailureHandler handler) {
		XAsyncCallback.handler = handler;
	}

	public static void setShowPopup(boolean showPopup) {
		XAsyncCallback.showPopup = showPopup;
	}

	@Override
	public void onFailure(Throwable caught) {
		if (handler != null)
			handler.onFailure(caught);

		if (!GWT.isProdMode())
			caught.printStackTrace();

		if (showPopup) {
			FailureConstants constants = GWT.create(FailureConstants.class);
			FailurePanel panel = new FailurePanel();

			if (caught instanceof StatusCodeException
					&& ((StatusCodeException) caught).getStatusCode() == 0)
				panel.setHTML(constants.connectionLost());
			else
				panel.setHTML(constants.unknownException());

			panel.show();
		}
	}
}
