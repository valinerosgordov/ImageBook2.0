package ru.imagebook.client.admin2.ctl;

import org.vectomatic.file.File;
import org.vectomatic.file.FileList;

import ru.imagebook.client.admin2.service.CodesImportRemoteService;
import ru.imagebook.client.admin2.service.CodesImportRemoteServiceAsync;
import ru.imagebook.client.admin2.view.CodesImportView;
import ru.minogin.upload.client.UploadCallback;
import ru.minogin.upload.client.XhrUploader;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.xhr.client.XMLHttpRequest;

public class CodesImportActivity extends AbstractActivity implements CodesImportPresenter {
	private final CodesImportView view;
	private final CodesImportRemoteServiceAsync service = GWT.create(CodesImportRemoteService.class);
	private EventBus eventBus;
	private final PlaceController placeController;

	public CodesImportActivity(PlaceController placeController, CodesImportView view) {
		this.placeController = placeController;
		this.view = view;
		view.setPresenter(this);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.eventBus = eventBus;
		panel.setWidget(view);
	}

	@Override
	public void onExcelFilesDrop(FileList files) {
		view.hideNotifications();
		view.notifyUploading();
		
		XhrUploader uploader = new XhrUploader();
		for (final File file : files) {
			uploader.upload(file, "/admin2/uploadCodes", new UploadCallback() {
				@Override
				public void onUploaded(XMLHttpRequest request) {
					view.hideNotifications();
					view.notifyUploaded(file.getName());
				}
				
				@Override
				public void onProgress(double value) {
				}
				
				@Override
				public void onError() {
					alertNotLoaded(file.getName());
				}
		   });
		}
	}
	
	private void alertNotLoaded(final String filaname) {
		view.hideNotifications();
		service.getImportErrorMessage(new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				view.alertNotLoaded(filaname, result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				view.alertNotLoaded(filaname, caught.getMessage());
			}
		});
	}
}
