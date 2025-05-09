package ru.imagebook.client.admin2.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("codesImport.remoteService")
public interface CodesImportRemoteService extends RemoteService {
	String getImportErrorMessage();
}
