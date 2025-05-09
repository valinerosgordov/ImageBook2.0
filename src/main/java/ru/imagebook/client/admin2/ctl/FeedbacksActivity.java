package ru.imagebook.client.admin2.ctl;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.xhr.client.XMLHttpRequest;
import org.vectomatic.file.File;
import org.vectomatic.file.FileList;
import ru.imagebook.client.admin2.service.CodesImportRemoteService;
import ru.imagebook.client.admin2.service.CodesImportRemoteServiceAsync;
import ru.imagebook.client.admin2.view.CodesImportView;
import ru.imagebook.client.admin2.view.FeedbacksView;
import ru.minogin.upload.client.UploadCallback;
import ru.minogin.upload.client.XhrUploader;

public class FeedbacksActivity extends AbstractActivity implements FeedbacksPresenter {
	private final FeedbacksView view;
	private EventBus eventBus;
	private final PlaceController placeController;

	public FeedbacksActivity(PlaceController placeController, FeedbacksView view) {
		this.placeController = placeController;
		this.view = view;
		view.setPresenter(this);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.eventBus = eventBus;
		panel.setWidget(view);
        view.show();
	}


}
