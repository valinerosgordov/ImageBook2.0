package ru.minogin.core.client.gxt.form;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface HtmlFieldServiceAsync {
	void getEditor(String value, AsyncCallback<String> callback);
}
