package ru.minogin.core.client.gxt.form;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("htmlField.remoteService")
public interface HtmlFieldService extends RemoteService {
	String getEditor(String value);
}
