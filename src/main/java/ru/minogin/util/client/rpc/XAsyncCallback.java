package ru.minogin.util.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;

public abstract class XAsyncCallback<T> implements AsyncCallback<T> {
	private static String unknownExceptionHTML;

	public static void setUnknownExceptionHTML(String unknownExceptionHTML) {
		XAsyncCallback.unknownExceptionHTML = unknownExceptionHTML;
	}

	@Override
	public void onFailure(Throwable caught) {
		caught.printStackTrace();

		FailureConstants constants = GWT.create(FailureConstants.class);
		FailurePanel panel = new FailurePanel();

		if (caught instanceof StatusCodeException
				&& ((StatusCodeException) caught).getStatusCode() == 0)
			panel.setHTML(constants.connectionLost());
		else if (unknownExceptionHTML != null)
			panel.setHTML(unknownExceptionHTML);
		else
			panel.setHTML(constants.unknownException());

		panel.show();
	}
}
