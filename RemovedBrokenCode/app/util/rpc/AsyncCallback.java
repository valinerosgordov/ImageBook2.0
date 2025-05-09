package ru.imagebook.client.app.util.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.StatusCodeException;

import ru.imagebook.client.app.view.common.Notify;
import ru.minogin.gwt.client.rpc.FailureConstants;


public abstract class AsyncCallback<T> implements com.google.gwt.user.client.rpc.AsyncCallback<T> {
    public AsyncCallback() {
    }

    public void onFailure(Throwable caught) {
        FailureConstants failureConstants = GWT.create(FailureConstants.class);

        if (caught instanceof StatusCodeException && ((StatusCodeException) caught).getStatusCode() == 0) {
            Notify.error(failureConstants.connectionLost());
        } else {
            Notify.error(failureConstants.unknownException());
        }
    }
}