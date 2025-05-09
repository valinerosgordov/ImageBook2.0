package ru.imagebook.client.admin2.view;

import ru.imagebook.client.admin2.ctl.CodesImportPresenter;

import com.google.gwt.user.client.ui.IsWidget;

public interface CodesImportView extends IsWidget {
	void setPresenter(CodesImportPresenter presenter);

	void notifyUploading();

	void hideNotifications();
	
	void alertNotLoaded(String filename, String errorMsg);

	void notifyUploaded(String filename);
}
