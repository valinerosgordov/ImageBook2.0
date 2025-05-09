package ru.imagebook.client.admin2.view;

import com.google.gwt.i18n.client.Messages;

public interface CodesImportConstants extends Messages {
	
	String beginUploading();
	
	String uploadError(String filename, String errorMsg);
	
	String uploadDone(String filename);	
}