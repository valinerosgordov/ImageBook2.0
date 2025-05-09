package ru.imagebook.client.admin2.view;

import ru.imagebook.client.admin2.ctl.CodesImportPresenter;
import ru.minogin.ui.client.field.upload.ActiveFileUpload;
import ru.minogin.ui.client.files.DropFilesEvent;
import ru.minogin.ui.client.files.DropFilesHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class CodesImportViewImpl implements CodesImportView {
	interface XUiBinder extends UiBinder<Widget, CodesImportViewImpl> {
	}

	private static XUiBinder uiBinder = GWT.create(XUiBinder.class);
	
	private CodesImportPresenter presenter;

	@UiField
	ActiveFileUpload uploadField;
	@UiField
	Label infoLabel;
	@UiField
	Label errorLabel;
	
	private CodesImportConstants constants = GWT.create(CodesImportConstants.class);

	@Override
	public Widget asWidget() {
		Widget ui = uiBinder.createAndBindUi(this);
		
		uploadField.addDropFilesHandler(new DropFilesHandler() {
			@Override
			public void onDropFiles(DropFilesEvent event) {
				presenter.onExcelFilesDrop(event.getFiles());
			}
		});
		
		return ui;
	}

	@Override
	public void setPresenter(CodesImportPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void notifyUploading() {
		uploadField.setVisible(false); // hide to prevent multiple file upload
		
		infoLabel.setVisible(true);
		infoLabel.setText(constants.beginUploading());
	}
	
	@Override
	public void hideNotifications() {
		infoLabel.setVisible(false);
		errorLabel.setVisible(false);
	}
	
	@Override
	public void alertNotLoaded(String filename, String errorMsg) {
		uploadField.setVisible(true);
		
		errorLabel.setVisible(true);
		errorLabel.setText(constants.uploadError(filename, errorMsg));
	}
	
	@Override
	public void notifyUploaded(String filename) {
		uploadField.setVisible(true);
		
		infoLabel.setVisible(true);
		infoLabel.setText(constants.uploadDone(filename));
	}
}
