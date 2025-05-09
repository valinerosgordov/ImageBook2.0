package ru.minogin.core.client.app.failure;

import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.exception.Exceptions;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * <p>
 * Use this class as a standard {@link AsyncCallback} implementation. In a case
 * of remote failure a standard error message will be shown to the user.
 * </p>
 * 
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>
 * void myMethod1() {
 *   service.loadItems(int categoryId, new XAsyncCallback() {
 *     At Override
 *     public void onSuccess(List<Item> items) {
 *       ...
 *     }
 *   });
 * }
 * </pre>
 * 
 * <pre>
 * void myMethod2() {
 *   service.loadItems(int categoryId, new XAsyncCallback() {
 *     At Override
 *     public void onSuccess(List<Item> items) {
 *       ...
 *     }
 * 
 *     At Override
 *     public void onFailure(Throwable caught) {
 *       if (caught instanceof KnownException)
 *         ... // handle known exception
 *       else
 *         super.onFailure(caught);
 *     }
 *   });
 * }
 * </pre>
 * 
 * @author Andrey Minogin
 */
public abstract class XAsyncCallback<T> implements AsyncCallback<T> {
	private CommonConstants commonConstants = GWT.create(CommonConstants.class);
	private FailureMessages constants = GWT.create(FailureMessages.class);

	@Override
	public void onFailure(Throwable caught) {
		Exceptions.rethrow(caught);
		// new MessageBox(commonConstants.error(), constants.failure(new Date()),
		// commonConstants).show();
	}
}
