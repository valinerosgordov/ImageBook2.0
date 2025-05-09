package ru.minogin.core.client.rpc;

import ru.minogin.core.client.exception.RethrownException;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.StatusCodeException;

public abstract class AbstractAsyncCallback<T> implements AsyncCallback<T> {
	@Override
	final public void onFailure(Throwable caught) {
		if (caught instanceof StatusCodeException) {
			StatusCodeException sce = (StatusCodeException) caught;
			if (sce.getStatusCode() == 500)
				onException(new RuntimeException());
			else {
				onDisconnect();
			}
		}
		else if (caught instanceof IncompatibleRemoteServiceException)
			onIncompatibleVersion();
		else if (caught instanceof RethrownException) {
			RethrownException rethrown = (RethrownException) caught;
			onException(rethrown.getException());
		}
		else if (caught instanceof Exception)
			onException((Exception) caught);
	}

	public abstract void onException(Exception e);

	public abstract void onDisconnect();

	public abstract void onIncompatibleVersion();
}
