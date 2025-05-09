package ru.imagebook.client.admin2.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import ru.imagebook.client.admin2.ctl.CodesImportPresenter;
import ru.imagebook.client.admin2.ctl.FeedbacksPresenter;
import ru.minogin.ui.client.field.upload.ActiveFileUpload;
import ru.minogin.ui.client.files.DropFilesEvent;
import ru.minogin.ui.client.files.DropFilesHandler;

public class FeedbacksViewImpl implements FeedbacksView {
	interface XUiBinder extends UiBinder<Widget, FeedbacksViewImpl> {}

	private static XUiBinder uiBinder = GWT.create(XUiBinder.class);
	
	private FeedbacksPresenter presenter;

	@Override
	public Widget asWidget() {
		return uiBinder.createAndBindUi(this);
	}

	@Override
	public void setPresenter(FeedbacksPresenter presenter) {
		this.presenter = presenter;
	}

    @Override
    public void show() {
        setUp();
    }

    public static native void setUp()/*-{
        $wnd.require(['/static/web/js/feedback/admin/main.js'], function(main){
            $wnd.require(['angular', 'feedbackAdminApp', 'jquery'], function(angular, app, $) {

                var $appContainer = $('#feedbackAdminApp');
                $appContainer.append('<div feedback-admin-controller-wrapper></div>');

                $wnd.angular.element().ready(function () {
                    $wnd.angular.bootstrap($appContainer, [app.name]);
                });
            });
        });
    }-*/;

}
